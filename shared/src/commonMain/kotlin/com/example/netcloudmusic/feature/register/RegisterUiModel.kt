package com.example.netcloudmusic.feature.register

/**
 * 注册界面输入数据模型
 */
data class RegisterUiModel(
    val nickname: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
)
