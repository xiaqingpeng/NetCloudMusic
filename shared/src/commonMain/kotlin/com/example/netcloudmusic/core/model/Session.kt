package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 登录成功后的会话信息。
 */
@Serializable
data class Session(
    val userId: String,
    val session: String,
    val chatToken: String,
    val user: User,
)
