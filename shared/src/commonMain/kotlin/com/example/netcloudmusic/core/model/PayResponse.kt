package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 支付参数
 * 网络请求响应
 */
@Serializable
data class PayResponse(
    /**
     * 支付渠道
     */
    val channel: Int = 0,

    /**
     * 支付宝参数
     */
    val pay: String? = null,

    /**
     * 微信支付参数
     */
    val wechatPay: WechatPay? = null,
)
