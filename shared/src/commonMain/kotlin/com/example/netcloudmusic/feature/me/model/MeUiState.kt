package com.example.netcloudmusic.feature.me.model

import com.example.netcloudmusic.core.model.User

/**
 * "我的"页面 UI 状态
 */
data class MeUiState(
    val isLoggedIn: Boolean = false,
    val user: User? = null,
)
