package com.example.netcloudmusic.core.model

import androidx.compose.runtime.Immutable
import com.example.netcloudmusic.core.util.Constant
import netcloudmusic.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * 支付渠道配置
 *
 * @param icon 渠道图标资源
 * @param title 渠道名称资源
 * @param channel 渠道常量
 */
@Immutable
data class PayChannel(
    val icon: DrawableResource,
    val title: StringResource,
    val channel: Int,
)

/**
 * 支付渠道列表
 * 
 * 包含：支付宝、微信、花呗分期
 */
val payChannels = listOf(
    PayChannel(
        icon = Res.drawable.pay_aliapy,
        title = Res.string.alipay,
        channel = Constant.ALIPAY,
    ),
    PayChannel(
        icon = Res.drawable.pay_wechat,
        title = Res.string.wechat,
        channel = Constant.WECHAT,
    ),
    PayChannel(
        icon = Res.drawable.pay_huabei_stage,
        title = Res.string.huabei_stages,
        channel = Constant.HUABEI_STAGE,
    ),
)
