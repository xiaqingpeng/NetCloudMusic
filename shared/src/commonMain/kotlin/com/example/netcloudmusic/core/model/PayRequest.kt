package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 请求支付信息参数
 */
@Serializable
data class PayRequest(
    val id: String,

    /**
     * 支付渠道
     */
    val channel: Int,

    /**
     * 支付平台
     * 默认值为android
     * 且不能更改
     * 因为Android平台的来说肯定就是Android
     */
    val origin: Int,
)
