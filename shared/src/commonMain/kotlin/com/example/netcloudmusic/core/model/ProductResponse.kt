package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val code: Int,
    val data: List<Product>
)
