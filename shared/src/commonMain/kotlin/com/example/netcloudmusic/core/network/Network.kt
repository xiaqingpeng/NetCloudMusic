package com.example.netcloudmusic.core.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import com.example.netcloudmusic.core.util.Constant
import kotlinx.serialization.json.Json

val NetworkJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false   // null 字段不序列化，与 Compose 版 Retrofit 行为一致
    encodeDefaults = true  // 值等于默认值的字段也序列化（如 default=false），与 Compose 版 Retrofit+Gson 行为一致
}

object Network {
    val httpClient: HttpClient by lazy { createHttpClient() }

    /**
     * 用于下载公开图片资源的客户端。
     *
     * 商品图片所在的 CDN 可能会拒绝携带业务 Authorization 的请求。
     * 因此分享图片不能复用 [httpClient]，否则部分平台会得到 403。
     */
    val imageHttpClient: HttpClient by lazy { createImageHttpClient() }
}

expect fun platformHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient

private fun createHttpClient(): HttpClient = platformHttpClient {
    install(DefaultRequest) {
        val session = NetworkSession.session
        if (session.isNotBlank()) {
            headers.append(Constant.HEADER_AUTH, session)
        }
    }
    install(ContentNegotiation) {
        json(NetworkJson)
    }
}

private fun createImageHttpClient(): HttpClient = platformHttpClient {
    install(DefaultRequest) {
        headers.append("User-Agent", "NetCloudMusic/1.0")
        headers.append("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8")
    }
}
