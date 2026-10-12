package com.example.netcloudmusic.core.data.repository

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

    private val json = Json { ignoreUnknownKeys = true }

    init {
        UserSessionStorage.loadSession()?.let { NetworkSession.session = it }
        UserSessionStorage.loadUser()?.let { value ->
            runCatching { json.decodeFromString<User>(value) }.getOrNull()?.let { _userState.value = it }
        }
    }

    fun login(username: String, password: String): Flow<NetworkResponse<Session>> = flow {
        val user = User(
            phone = username.takeIf { it.matches(Regex("^1\\d{10}$")) },
            email = username.takeUnless { it.matches(Regex("^1\\d{10}$")) },
            password = password,
        )
        val response = dataSource.login(user)
        response.data?.let { session ->
            NetworkSession.session = session.session
            _userState.value = session.user
            UserSessionStorage.save(session.session, json.encodeToString(session.user))
        }
        emit(response)
    }

    fun refreshUserState() {
        // 持久化由各平台宿主补充；当前共享层至少保证登录后的状态可观察。
        _userState.value = _userState.value
    }

    fun isLoggedIn(): Boolean = !NetworkSession.session.isNullOrBlank()

    fun logout() {
        NetworkSession.clear()
        UserSessionStorage.clear()
        _userState.value = null
    }
}
