package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 重置密码请求参数
 */
@Serializable
data class ResetPasswordRequest(
    val phone: String,
    val code: String,
    val password: String,
)
