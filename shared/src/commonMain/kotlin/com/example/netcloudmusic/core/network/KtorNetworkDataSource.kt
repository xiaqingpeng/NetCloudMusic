package com.example.netcloudmusic.core.network

import com.example.netcloudmusic.core.config.Config
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.model.Session
import com.example.netcloudmusic.core.model.User
import com.example.netcloudmusic.core.model.response.NetworkPageData
import com.example.netcloudmusic.core.model.response.NetworkResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

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

    override suspend fun login(data: User): NetworkResponse<Session> {
        return httpClient.post("${Config.ENDPOINT}v1/login") {
            setBody(data)
        }.body()
    }
}
