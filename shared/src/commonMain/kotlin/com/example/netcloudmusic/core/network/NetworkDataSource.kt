package com.example.netcloudmusic.core.network

import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.model.Session
import com.example.netcloudmusic.core.model.User
import com.example.netcloudmusic.core.model.response.NetworkPageData
import com.example.netcloudmusic.core.model.response.NetworkResponse

/**
 * 应用业务 API。
 */
interface NetworkDataSource {
    suspend fun products(
        app: Int? = null,
        query: String? = null,
        size: String? = null,
        page: String? = null,
    ): NetworkResponse<NetworkPageData<Product>>

    suspend fun productDetail(id: String): NetworkResponse<Product>

    suspend fun login(data: User): NetworkResponse<Session>
}
