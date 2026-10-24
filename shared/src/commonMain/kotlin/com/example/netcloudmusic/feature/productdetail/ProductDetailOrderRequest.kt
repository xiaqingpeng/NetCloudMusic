package com.example.netcloudmusic.feature.productdetail

/** 商品详情模块发出的下单选择信息。 */
data class ProductDetailOrderRequest(
    val productId: String,
    val stockId: String?,
    val count: Int,
)
