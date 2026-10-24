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

    /** 商品列表（同步方法，用于 Discovery） */
    suspend fun productsSync(
        page: Int,
        size: Int,
    ): NetworkResponse<NetworkPageData<Product>> =
        dataSource.products(page = page.toString(), size = size.toString())

    /** 商品详情（同步方法，用于 ProductDetail） */
    suspend fun productDetailSync(id: String): NetworkResponse<Product> =
        dataSource.productDetail(id)
}
