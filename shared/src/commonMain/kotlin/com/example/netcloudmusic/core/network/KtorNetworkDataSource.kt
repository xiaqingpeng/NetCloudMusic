package com.example.netcloudmusic.core.network

import com.example.netcloudmusic.core.config.Config
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
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Ktor 实现，保持与 Compose 工程 Retrofit DataSource 相同的接口。
 */
class KtorNetworkDataSource(
    private val httpClient: io.ktor.client.HttpClient = Network.httpClient,
) : NetworkDataSource {

    override suspend fun products(
        app: Int?,
        query: String?,
        size: String?,
        page: String?,
    ): NetworkResponse<NetworkPageData<Product>> {
        return httpClient.get("${Config.ENDPOINT}v1/products/page") {
            app?.let { parameter("app", it) }
            query?.let { parameter("query", it) }
            size?.let { parameter("size", it) }
            page?.let { parameter("page", it) }
        }.body()
    }

    override suspend fun productDetail(id: String): NetworkResponse<Product> {
        println("KtorNetworkDataSource: 请求商品详情 - id=$id")
        val url = "${Config.ENDPOINT}v1/products/info"
        println("KtorNetworkDataSource: URL=$url")
        return try {
            val response = httpClient.get(url) {
                parameter("id", id)
            }.body<NetworkResponse<Product>>()
            println("KtorNetworkDataSource: 商品详情响应 - isSucceeded=${response.isSucceeded}, status=${response.status}")
            response
        } catch (e: Exception) {
            println("KtorNetworkDataSource: 商品详情异常 - ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    // ── 首页区块数据 ───────────────────────────────────────────────────────────

    override suspend fun indexes(app: Int?): NetworkResponse<NetworkPageData<com.example.netcloudmusic.core.model.ViewData>> {
        val url = "${Config.ENDPOINT}v1/indexes"
        println("KtorNetworkDataSource: 请求 indexes - URL=$url, app=$app")
        return try {
            val response = httpClient.get(url) {
                app?.let { parameter("app", it) }
            }.body<NetworkResponse<NetworkPageData<com.example.netcloudmusic.core.model.ViewData>>>()
            println("KtorNetworkDataSource: indexes 响应 - isSucceeded=${response.isSucceeded}, status=${response.status}, message=${response.message}")
            response
        } catch (e: Exception) {
            println("KtorNetworkDataSource: indexes 异常 - ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    // ── 购物车 ──────────────────────────────────────────────────────────────────

    override suspend fun carts(): NetworkResponse<NetworkPageData<com.example.netcloudmusic.core.model.Cart>> {
        return httpClient.get("${Config.ENDPOINT}v1/carts/list").body()
    }

    override suspend fun addProductToCart(cart: com.example.netcloudmusic.core.model.Cart): NetworkResponse<com.example.netcloudmusic.core.model.Cart> {
        return httpClient.post("${Config.ENDPOINT}v1/carts/add") {
            contentType(ContentType.Application.Json)
            setBody(cart)
        }.body()
    }

    override suspend fun deleteCarts(ids: List<String>): NetworkResponse<Any> {
        return httpClient.post("${Config.ENDPOINT}v1/carts/delete") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("ids" to ids))
        }.body()
    }

    // ── 账号 ──────────────────────────────────────────────────────────────────

    override suspend fun login(data: User): NetworkResponse<Session> {
        return httpClient.post("${Config.ENDPOINT}v1/login") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

    override suspend fun sendCode(data: User): NetworkResponse<SendCodeResponse> {
        return httpClient.post("${Config.ENDPOINT}v1/codes") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

    override suspend fun loginByCode(data: User): NetworkResponse<Session> {
        // 复用 v1/login，通过 User.style=0 区分验证码登录
        return httpClient.post("${Config.ENDPOINT}v1/login") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

    override suspend fun register(data: User): NetworkResponse<RegisterResponse> {
        return httpClient.post("${Config.ENDPOINT}v1/users/add") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

    override suspend fun userDetail(id: String): NetworkResponse<User> {
        return httpClient.get("${Config.ENDPOINT}v1/users/info") {
            parameter("id", id)
        }.body()
    }

    override suspend fun updateUser(data: User): NetworkResponse<BaseModel> {
        return httpClient.post("${Config.ENDPOINT}v1/users/update") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

    override suspend fun sendForgotPasswordCode(phone: String): NetworkResponse<SendCodeResponse> {
        return httpClient.post("${Config.ENDPOINT}v1/codes") {
            contentType(ContentType.Application.Json)
            setBody(User(phone = phone, style = 10)) // style=10 表示忘记密码验证码
        }.body()
    }

    override suspend fun resetPassword(phone: String, code: String, newPassword: String): NetworkResponse<BaseModel> {
        println("KtorNetworkDataSource: ===== 重置密码请求 =====")
        println("KtorNetworkDataSource: phone=$phone, code=$code")
        val requestBody = com.example.netcloudmusic.core.model.ResetPasswordRequest(
            phone = phone,
            code = code,
            password = newPassword
        )
        println("KtorNetworkDataSource: Request URL: ${Config.ENDPOINT}v1/users/reset_password")
        println("KtorNetworkDataSource: Request Body: $requestBody")
        
        val result = httpClient.post("${Config.ENDPOINT}v1/users/reset_password") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body<NetworkResponse<BaseModel>>()
        
        println("KtorNetworkDataSource: ===== 响应 =====")
        println("KtorNetworkDataSource: isSucceeded=${result.isSucceeded}")
        println("KtorNetworkDataSource: message=${result.message}")
        println("KtorNetworkDataSource: data=${result.data}")
        println("KtorNetworkDataSource: status=${result.status}")
        return result
    }

    // ── 收货地址 ──────────────────────────────────────────────────────────────

    override suspend fun getAddressList(): NetworkResponse<NetworkPageData<Address>> {
        return httpClient.get("${Config.ENDPOINT}v1/addresses/list").body()
    }

    override suspend fun addressDetail(id: String): NetworkResponse<Address> {
        return httpClient.get("${Config.ENDPOINT}v1/addresses/info") {
            parameter("id", id)
        }.body()
    }

    override suspend fun createAddress(data: Address): NetworkResponse<BaseId> {
        return httpClient.post("${Config.ENDPOINT}v1/addresses/add") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

    override suspend fun updateAddress(data: Address): NetworkResponse<BaseId> {
        return httpClient.post("${Config.ENDPOINT}v1/addresses/update") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

    override suspend fun deleteAddress(id: String): NetworkResponse<BaseModel> {
        return httpClient.post("${Config.ENDPOINT}v1/addresses/delete") {
            parameter("id", id)
        }.body()
    }

    // ── 订单 ──────────────────────────────────────────────────────────────────

    override suspend fun confirmOrder(request: com.example.netcloudmusic.core.model.OrderRequest): NetworkResponse<com.example.netcloudmusic.core.model.ConfirmOrderResponse> {
        return httpClient.post("${Config.ENDPOINT}v1/orders/confirm") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun createOrder(request: com.example.netcloudmusic.core.model.OrderRequest): NetworkResponse<com.example.netcloudmusic.core.model.Order> {
        return httpClient.post("${Config.ENDPOINT}v1/orders/add") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun orderDetail(orderId: String): NetworkResponse<com.example.netcloudmusic.core.model.Order> {
        return httpClient.get("${Config.ENDPOINT}v1/orders/info") {
            parameter("id", orderId)
        }.body()
    }

    override suspend fun orderPay(request: com.example.netcloudmusic.core.model.PayRequest): NetworkResponse<com.example.netcloudmusic.core.model.PayResponse> {
        return httpClient.post("${Config.ENDPOINT}v1/orders/pay") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    // ── 分类 ──────────────────────────────────────────────────────────────────

    override suspend fun categories(parent: String?): NetworkResponse<NetworkPageData<com.example.netcloudmusic.core.model.Category>> {
        println("KtorNetworkDataSource: 请求分类列表，parent=$parent")
        val url = "${Config.ENDPOINT}v1/categories/list"
        println("KtorNetworkDataSource: URL=$url")
        return try {
            val response = httpClient.get(url) {
                parent?.let { parameter("parent", it) }
            }.body<NetworkResponse<NetworkPageData<com.example.netcloudmusic.core.model.Category>>>()
            println("KtorNetworkDataSource: 分类响应 - isSucceeded=${response.isSucceeded}, status=${response.status}, message=${response.message}")
            println("KtorNetworkDataSource: data=${response.data}, list size=${response.data?.list?.size}")
            response
        } catch (e: Exception) {
            println("KtorNetworkDataSource: 请求分类失败: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}
