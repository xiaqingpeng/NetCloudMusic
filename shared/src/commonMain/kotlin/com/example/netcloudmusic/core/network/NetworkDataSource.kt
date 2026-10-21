package com.example.netcloudmusic.core.network

import com.example.netcloudmusic.core.model.Address
import com.example.netcloudmusic.core.model.BaseId
import com.example.netcloudmusic.core.model.BaseModel
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.model.RegisterResponse
import com.example.netcloudmusic.core.model.SendCodeResponse
import com.example.netcloudmusic.core.model.Session
import com.example.netcloudmusic.core.model.User
import com.example.netcloudmusic.core.model.response.NetworkPageData
import com.example.netcloudmusic.core.model.response.NetworkResponse

/**
 * 应用业务 API。
 */
interface NetworkDataSource {

    // ── 商品 ──────────────────────────────────────────────────────────────────

    suspend fun products(
        app: Int? = null,
        query: String? = null,
        size: String? = null,
        page: String? = null,
    ): NetworkResponse<NetworkPageData<Product>>

    suspend fun productDetail(id: String): NetworkResponse<Product>

    // ── 账号 ──────────────────────────────────────────────────────────────────

    /** 用户名/密码登录 */
    suspend fun login(data: User): NetworkResponse<Session>

    /** 发送短信验证码 */
    suspend fun sendCode(data: User): NetworkResponse<SendCodeResponse>

    /** 验证码登录 */
    suspend fun loginByCode(data: User): NetworkResponse<Session>

    /** 注册新账号 */
    suspend fun register(data: User): NetworkResponse<RegisterResponse>

    /** 获取用户详情 */
    suspend fun userDetail(id: String): NetworkResponse<User>

    /** 更新用户资料 */
    suspend fun updateUser(data: User): NetworkResponse<BaseModel>

    // ── 收货地址 ──────────────────────────────────────────────────────────────

    suspend fun getAddressList(): NetworkResponse<NetworkPageData<Address>>
    suspend fun addressDetail(id: String): NetworkResponse<Address>
    suspend fun createAddress(data: Address): NetworkResponse<BaseId>
    suspend fun updateAddress(data: Address): NetworkResponse<BaseId>
    suspend fun deleteAddress(id: String): NetworkResponse<BaseModel>
}
