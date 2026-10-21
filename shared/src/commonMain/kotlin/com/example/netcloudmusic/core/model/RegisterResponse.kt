package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 注册成功后服务端返回的信息
 */
@Serializable
data class RegisterResponse(
    /** 新用户 ID */
    val id: String,
)
