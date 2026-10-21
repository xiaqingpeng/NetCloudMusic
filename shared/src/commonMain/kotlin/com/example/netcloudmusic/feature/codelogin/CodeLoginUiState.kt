package com.example.netcloudmusic.feature.codelogin

/**
 * 验证码登录界面状态
 */
sealed interface CodeLoginUiState {
    data object None : CodeLoginUiState
    data object Loading : CodeLoginUiState
    data object Success : CodeLoginUiState
    data class Error(val message: String) : CodeLoginUiState
}

/**
 * 发送验证码按钮状态
 */
sealed interface SendCodeUiState {
    data object Idle : SendCodeUiState
    data object Sending : SendCodeUiState
    data class Countdown(val remainSeconds: Int) : SendCodeUiState
    data class Error(val message: String?) : SendCodeUiState
}
