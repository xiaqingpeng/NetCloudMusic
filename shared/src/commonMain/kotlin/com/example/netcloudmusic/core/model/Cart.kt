package com.example.netcloudmusic.core.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Cart(
    val id: String? = null,
    val product: Product? = null,
    val count: Int = 1,
    val productId: String? = null,
    val stock: Stock? = null,
    val stockId: String? = null,
    val update: Boolean = false,
    val selected: Boolean = true,
)
