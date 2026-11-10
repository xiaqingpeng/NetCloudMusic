package com.example.netcloudmusic.core.network

import com.example.netcloudmusic.core.model.Address
import com.example.netcloudmusic.core.model.BaseId
import com.example.netcloudmusic.core.model.BaseModel
import com.example.netcloudmusic.core.model.Cart
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

    // ── 首页区块数据 ───────────────────────────────────────────────────────────

    /** 首页数据（Banner、功能按钮、大图、热门商品等） */
    suspend fun indexes(
        app: Int? = null,
    ): NetworkResponse<NetworkPageData<com.example.netcloudmusic.core.model.ViewData>>

    // ── 购物车 ──────────────────────────────────────────────────────────────────

    /** 获取购物车列表 */
    suspend fun carts(): NetworkResponse<NetworkPageData<Cart>>

    /** 添加商品到购物车 */
    suspend fun addProductToCart(cart: Cart): NetworkResponse<Cart>

    /** 删除购物车项 */
    suspend fun deleteCarts(ids: List<String>): NetworkResponse<Any>

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

    /** 发送忘记密码验证码 */
    suspend fun sendForgotPasswordCode(phone: String): NetworkResponse<SendCodeResponse>

    /** 重置密码 */
    suspend fun resetPassword(phone: String, code: String, newPassword: String): NetworkResponse<BaseModel>

    // ── 收货地址 ──────────────────────────────────────────────────────────────

    suspend fun getAddressList(): NetworkResponse<NetworkPageData<Address>>
    suspend fun addressDetail(id: String): NetworkResponse<Address>
    suspend fun createAddress(data: Address): NetworkResponse<BaseId>
    suspend fun updateAddress(data: Address): NetworkResponse<BaseId>
    suspend fun deleteAddress(id: String): NetworkResponse<BaseModel>

    // ── 订单 ──────────────────────────────────────────────────────────────────

    /** 预览订单（确认订单页面） */
    suspend fun confirmOrder(request: com.example.netcloudmusic.core.model.OrderRequest): NetworkResponse<com.example.netcloudmusic.core.model.ConfirmOrderResponse>

    /** 创建订单 */
    suspend fun createOrder(request: com.example.netcloudmusic.core.model.OrderRequest): NetworkResponse<com.example.netcloudmusic.core.model.Order>

    /** 订单详情 */
    suspend fun orderDetail(orderId: String): NetworkResponse<com.example.netcloudmusic.core.model.Order>

    /** 订单列表（支持 status/page 等过滤参数） */
    suspend fun orders(params: Map<String, String> = emptyMap()): NetworkResponse<NetworkPageData<com.example.netcloudmusic.core.model.Order>>

    /** 请求支付 */
    suspend fun orderPay(request: com.example.netcloudmusic.core.model.PayRequest): NetworkResponse<com.example.netcloudmusic.core.model.PayResponse>

    // ── 分类 ──────────────────────────────────────────────────────────────────

    /** 获取分类列表 */
    suspend fun categories(parent: String? = null): NetworkResponse<NetworkPageData<com.example.netcloudmusic.core.model.Category>>
}
