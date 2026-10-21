package com.example.netcloudmusic.feature.register

/**
 * 注册界面状态
 */
sealed interface RegisterUiState {
    data object None : RegisterUiState
    data object Loading : RegisterUiState
    data object Success : RegisterUiState
    /** 轻量错误，通过 message 弹 Toast */
    data class Error(val message: String) : RegisterUiState
}
