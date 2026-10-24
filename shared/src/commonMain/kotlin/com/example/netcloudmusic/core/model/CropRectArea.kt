package com.example.netcloudmusic.core.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/** 裁剪区域点击热区数据 */
@Immutable
@Serializable
data class CropRectArea(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val uri: String? = null,
)
