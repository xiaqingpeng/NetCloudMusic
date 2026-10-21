package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 发送验证码接口响应。
 * 测试环境下服务端会在响应体里直接返回验证码，方便联调。
 */
@Serializable
data class SendCodeResponse(
    /** 验证码，仅测试环境返回 */
    val code: String? = null,
)
