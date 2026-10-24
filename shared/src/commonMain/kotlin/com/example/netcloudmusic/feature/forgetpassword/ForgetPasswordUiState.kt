package com.example.netcloudmusic.feature.forgetpassword

import androidx.compose.runtime.Stable

/**
 * 忘记密码页面主流程状态
 */
@Stable
sealed interface ForgetPasswordUiState {
    data object None : ForgetPasswordUiState
    data object Loading : ForgetPasswordUiState
    data object Success : ForgetPasswordUiState

    /** 轻量校验错误，通过 Snackbar 提示，data 为错误消息 */
    data class ErrorMessage(val message: String) : ForgetPasswordUiState

    /** 服务端/网络错误，触发 AlertDialog */
    data class Error(
        val res: Int? = null,
        val throwable: Throwable? = null,
        val data: Throwable = throwable ?: IllegalStateException(),
    ) : ForgetPasswordUiState
}

/**
 * 发送验证码按钮状态（与 CodeLoginUiState 保持一致）
 */
@Stable
sealed interface ForgetSendCodeUiState {
    data object Idle : ForgetSendCodeUiState
    data object Sending : ForgetSendCodeUiState
    data class Countdown(val remainSeconds: Int) : ForgetSendCodeUiState
    data class Error(val message: String?) : ForgetSendCodeUiState
}
