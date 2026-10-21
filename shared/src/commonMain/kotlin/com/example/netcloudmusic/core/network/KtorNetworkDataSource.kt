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
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
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
        return httpClient.get("${Config.ENDPOINT}v1/products/info") {
            parameter("id", id)
        }.body()
    }

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
}
