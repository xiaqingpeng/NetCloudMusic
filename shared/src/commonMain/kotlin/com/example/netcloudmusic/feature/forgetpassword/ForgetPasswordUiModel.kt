package com.example.netcloudmusic.feature.forgetpassword

/**
 * 忘记密码页面输入数据模型
 * 统一由 ViewModel 管理，避免在 UI 层使用 rememberSaveable
 */
data class ForgetPasswordUiModel(
    val phone: String = "",
    val code: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
)
