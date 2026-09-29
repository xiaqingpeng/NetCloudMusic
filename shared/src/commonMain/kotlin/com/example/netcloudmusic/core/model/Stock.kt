package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val id: String? = null,
    val ids: String? = null,
    val number: String? = null,
    val title: String? = null,
    val icon: String? = null,
    val price: Int = 0,
    val originPrice: Int = 0,
    val status: Int = 0,
    val stock: Long = 0,
    val weight: Float = 0F,
)