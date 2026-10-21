package com.example.netcloudmusic.feature.codelogin

/**
 * 验证码登录输入数据模型
 */
data class CodeLoginUiModel(
    val phone: String = "",
    val code: String = "",
)
