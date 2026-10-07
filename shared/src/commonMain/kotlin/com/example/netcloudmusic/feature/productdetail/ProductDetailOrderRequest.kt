package com.example.netcloudmusic.feature.productdetail

data class ProductDetailOrderRequest(
    val productId: String,
    val stockId: String?,
    val count: Int,
)
