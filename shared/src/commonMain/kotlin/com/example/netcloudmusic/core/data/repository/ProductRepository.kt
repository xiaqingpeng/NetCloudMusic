package com.example.netcloudmusic.core.data.repository

import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.model.response.NetworkPageData
import com.example.netcloudmusic.core.model.response.NetworkResponse
import com.example.netcloudmusic.core.network.NetworkDataSource

/** 商品领域仓库，隔离 ViewModel 与具体网络实现。 */
class ProductRepository(
    private val dataSource: NetworkDataSource,
) {
    suspend fun products(
        app: Int? = null,
        query: String? = null,
        size: String? = null,
        page: String? = null,
    ): NetworkResponse<NetworkPageData<Product>> =
        dataSource.products(app, query, size, page)

    suspend fun productDetail(id: String): NetworkResponse<Product> =
        dataSource.productDetail(id)
}
