package com.example.netcloudmusic.core.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val NetworkJson = Json {
    ignoreUnknownKeys = true
}

object Network {
    val httpClient: HttpClient by lazy { createHttpClient() }
}

internal expect fun platformHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient

private fun createHttpClient(): HttpClient = platformHttpClient {
    install(ContentNegotiation) {
        json(NetworkJson)
    }
}
