package com.example.netcloudmusic.core.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import com.example.netcloudmusic.util.Constant
import kotlinx.serialization.json.Json

val NetworkJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false   // null 字段不序列化，与 Compose 版 Retrofit 行为一致
    encodeDefaults = true  // 值等于默认值的字段也序列化（如 default=false），与 Compose 版 Retrofit+Gson 行为一致
}

object Network {
    val httpClient: HttpClient by lazy { createHttpClient() }
}

internal expect fun platformHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient

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
