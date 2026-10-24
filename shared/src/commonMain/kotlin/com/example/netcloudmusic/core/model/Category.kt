package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 分类
 */
@Serializable
data class Category(
    val id: String,
    val title: String,
    val icon: String? = null,
    val parentId: String? = null,
    val sort: Long? = null,
    val data: List<Category>? = null,
)
