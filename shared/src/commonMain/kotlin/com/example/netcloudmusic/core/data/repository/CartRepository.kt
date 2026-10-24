package com.example.netcloudmusic.core.data.repository

import com.example.netcloudmusic.core.model.Cart
import com.example.netcloudmusic.core.model.response.NetworkPageData
import com.example.netcloudmusic.core.model.response.NetworkResponse
import com.example.netcloudmusic.core.network.NetworkDataSource

/** 购物车领域仓库 */
class CartRepository(
    private val dataSource: NetworkDataSource,
) {
    /** 获取购物车列表（同步） */
    suspend fun cartsSync(): NetworkResponse<NetworkPageData<Cart>> =
        dataSource.carts()

    /** 添加商品到购物车（同步） */
    suspend fun addProductToCartSync(cart: Cart): NetworkResponse<Cart> =
        dataSource.addProductToCart(cart)

    /** 删除购物车项（同步） */
    suspend fun deleteCartsSync(ids: List<String>): NetworkResponse<Any> =
        dataSource.deleteCarts(ids)
}
