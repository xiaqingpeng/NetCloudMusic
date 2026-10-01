package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

@Serializable
data class CropRectData(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val uri: String? = null,
) {

}