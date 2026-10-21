package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 新增/更新操作成功后返回的 ID
 */
@Serializable
data class BaseId(val id: String)
