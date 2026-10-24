package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

@Serializable
data class CropData(
    val icon: String,
    val data: List<CropRectData>,
    val width: Int = 0,
    val height: Int = 0,
)
