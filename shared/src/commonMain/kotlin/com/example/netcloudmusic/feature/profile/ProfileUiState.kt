package com.example.netcloudmusic.feature.profile

/**
 * 个人资料界面状态
 */
sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data object Success : ProfileUiState
    data object Saving : ProfileUiState        // 保存中，防止重复点击
    data class Error(val message: String? = null) : ProfileUiState
}

/**
 * Toast 一次性事件
 */
sealed interface ProfileToastEvent {
    data class Text(val message: String) : ProfileToastEvent
}
