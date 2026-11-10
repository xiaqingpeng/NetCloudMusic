package com.example.netcloudmusic.core.data.repository

import com.example.netcloudmusic.core.model.BaseModel
import com.example.netcloudmusic.core.model.GithubUser
import com.example.netcloudmusic.core.model.RegisterResponse
import com.example.netcloudmusic.core.model.SendCodeResponse
import com.example.netcloudmusic.core.model.Session
import com.example.netcloudmusic.core.model.User
import com.example.netcloudmusic.core.model.response.NetworkResponse
import com.example.netcloudmusic.core.network.NetworkDataSource
import com.example.netcloudmusic.core.network.NetworkSession
import com.example.netcloudmusic.core.session.UserSessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** 用户数据仓库，统一管理登录、会话和当前用户状态。 */
class UserRepository(
    private val dataSource: NetworkDataSource,
) {
    private val _userState = MutableStateFlow<User?>(null)
    val userState = _userState.asStateFlow()

    // 用于本地持久化（需要保存完整字段，包括有默认值的字段）
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    init {
        // github_ 前缀是 GitHub 本地登录标识，不是业务 session，不写入 NetworkSession
        UserSessionStorage.loadSession()?.let { session ->
            if (session.isNotBlank() && !session.startsWith("github_")) {
                NetworkSession.session = session
            }
        }
        UserSessionStorage.loadUser()?.let { value ->
            runCatching { json.decodeFromString<User>(value) }.getOrNull()?.let { _userState.value = it }
        }
    }

    // ── 用户名/密码登录 ──────────────────────────────────────────────────────

    fun login(username: String, password: String): Flow<NetworkResponse<Session>> = flow {
        val user = User(
            phone = username.takeIf { it.matches(Regex("^1\\d{10}$")) },
            email = username.takeUnless { it.matches(Regex("^1\\d{10}$")) },
            password = password,
        )
        val response = dataSource.login(user)
        response.data?.let { session -> persistSession(session) }
        emit(response)
    }

    // ── 验证码登录 ────────────────────────────────────────────────────────────

    /** 发送短信验证码。style = 0 表示登录验证码。 */
    fun sendCode(phone: String): Flow<NetworkResponse<SendCodeResponse>> = flow {
        val user = User(phone = phone, style = 0)
        emit(dataSource.sendCode(user))
    }

    /** 使用手机号 + 验证码登录。 */
    fun loginByCode(phone: String, code: String): Flow<NetworkResponse<Session>> = flow {
        val user = User(phone = phone, code = code, style = 0)
        val response = dataSource.loginByCode(user)
        response.data?.let { session -> persistSession(session) }
        emit(response)
    }

    // ── 第三方登录 ────────────────────────────────────────────────────────────

    /**
     * GitHub 本地登录。
     * 没有业务服务端 session，写入 github_ 前缀本地标识；不要写入 NetworkSession。
     */
    fun loginWithGithub(githubUser: GithubUser, githubToken: String) {
        val user = User(
            id = "github_${githubUser.id}",
            nickname = githubUser.name ?: githubUser.login,
            icon = githubUser.avatarUrl,
            detail = githubUser.bio,
            email = githubUser.email,
            style = 20,
            token = githubToken,
            openid = githubUser.login,
        )
        UserSessionStorage.save("github_${githubToken.take(20)}", json.encodeToString(user))
        _userState.value = user
    }

    // ── 注册 ─────────────────────────────────────────────────────────────────

    /**
     * 注册新账号，注册成功后自动调用登录接口，返回登录 Session。
     */
    fun register(user: User): Flow<NetworkResponse<Session>> = flow {
        val registerResponse = dataSource.register(user)
        if (registerResponse.isSucceeded && registerResponse.data != null) {
            val loginResponse = dataSource.login(user)
            loginResponse.data?.let { session -> persistSession(session) }
            emit(loginResponse)
        } else {
            emit(
                NetworkResponse(
                    data = null,
                    status = registerResponse.status,
                    message = registerResponse.message,
                )
            )
        }
    }

    // ── 忘记密码 ──────────────────────────────────────────────────────────────

    /** 发送忘记密码验证码。style = 10 表示忘记密码验证码。 */
    fun sendForgotPasswordCode(phone: String): Flow<NetworkResponse<SendCodeResponse>> = flow {
        emit(dataSource.sendForgotPasswordCode(phone))
    }

    /** 重置密码 */
    fun resetPassword(phone: String, code: String, newPassword: String): Flow<NetworkResponse<BaseModel>> = flow {
        emit(dataSource.resetPassword(phone, code, newPassword))
    }

    // ── 用户详情 / 更新 ───────────────────────────────────────────────────────

    /** 获取用户详情（网络请求）。 */
    fun userDetail(id: String): Flow<NetworkResponse<User>> = flow {
        emit(dataSource.userDetail(id))
    }

    /** 更新用户资料，成功后同步更新本地缓存和 userState。 */
    fun updateUser(user: User): Flow<NetworkResponse<BaseModel>> = flow {
        val response = dataSource.updateUser(user)
        if (response.isSucceeded) {
            val updated = (_userState.value ?: user).copy(
                nickname = user.nickname,
                icon = user.icon,
                bg = user.bg,
                gender = user.gender,
                birthday = user.birthday,
                detail = user.detail,
                province = user.province,
                provinceCode = user.provinceCode,
                city = user.city,
                cityCode = user.cityCode,
                area = user.area,
                areaCode = user.areaCode,
            )
            _userState.value = updated
            val localSession = UserSessionStorage.loadSession().orEmpty().ifBlank {
                NetworkSession.session
            }
            UserSessionStorage.save(localSession, json.encodeToString(updated))
        }
        emit(response)
    }

    // ── 其他 ──────────────────────────────────────────────────────────────────

    fun refreshUserState() {
        _userState.value = _userState.value
    }

    /** 业务 session 非空，或本地已有用户（含 GitHub 本地登录）。 */
    fun isLoggedIn(): Boolean =
        NetworkSession.session.isNotBlank() || _userState.value != null

    /**
     * 是否具备商城业务登录态。
     * 订单 / 支付 / 地址等接口依赖 Authorization，GitHub 本地登录不算。
     */
    fun hasBusinessSession(): Boolean =
        NetworkSession.session.isNotBlank() && !NetworkSession.session.startsWith("github_")

    fun getLocalUser(): User? = _userState.value

    fun logout() {
        NetworkSession.clear()
        UserSessionStorage.clear()
        _userState.value = null
    }

    // ── 私有辅助 ──────────────────────────────────────────────────────────────

    private fun persistSession(session: Session) {
        NetworkSession.session = session.session
        _userState.value = session.user
        UserSessionStorage.save(session.session, json.encodeToString(session.user))
    }
}
