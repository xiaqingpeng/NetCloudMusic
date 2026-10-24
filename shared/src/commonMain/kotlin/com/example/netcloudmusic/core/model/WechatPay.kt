package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 微信支付参数（服务端返回）
 *
 * 字段解释：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
 */
@Serializable
data class WechatPay(
    val appid: String? = null,

    val partnerId: String? = null,

    val prepayId: String? = null,

    val nonceStr: String? = null,

    val timestamp: String? = null,

    val packageVal: String? = null,
    val sign: String? = null,
)
