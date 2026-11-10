package com.example.netcloudmusic.core.model

import com.example.netcloudmusic.core.util.Constant
import kotlinx.serialization.Serializable

/**
 * 订单模型
 */
@Serializable
data class Order(
    /**
     * 订单状态
     */
    val status: Int = 0,

    /**
     * 商品总价
     */
    val totalPrice: Int = 0,

    /**
     * 价格
     */
    val price: Int = 0,

    /**
     * 订单来源
     * 一般订单来源不会返回给客户端
     * 我们这样返回来只是给大家演示如何显示这些字段而已
     * 也就是在那个平台创建的订单
     */
    val source: Int = 0,

    /**
     * 支付来源
     * 因为可能有创建订单是web网站
     * 用户是在手机上支付的
     */
    val origin: Int = 0,

    /**
     * 支付渠道
     */
    val channel: Int = 0,

    /**
     * 订单号
     */
    val number: String? = null,

    /**
     * 订单所关联的商品信息
     */
    val carts: List<Cart>? = null,

    /**
     * 地址
     */
    val address: Address? = null,

    val id: String = "",
    val created: String = "",
    val updated: String = "",
) {

    //region 辅助方法
    val priceFloat
        get() = price * 1.0 / 100

    val totalPriceFloat
        get() = totalPrice * 1.0 / 100

    /**
     * 是否已经支付成功
     */
    val isPaid: Boolean
        get() = status >= Constant.WAIT_SHIPPED

    /**
     * 是否是待支付
     */
    val isWaitPay: Boolean
        get() = status == Constant.WAIT_PAY

    /**
     * 是否待发货
     */
    val isShipped: Boolean
        get() = status == Constant.WAIT_SHIPPED

    /** 状态文字描述 */
    val statusText: String
        get() = when (status) {
            Constant.WAIT_PAY -> "待付款"
            Constant.CLOSE -> "已关闭"
            Constant.WAIT_SHIPPED -> "待发货"
            Constant.WAIT_RECEIVED -> "待收货"
            Constant.WAIT_COMMENT -> "待评价"
            Constant.COMPLETE -> "已完成"
            else -> "未知状态"
        }

    /**
     * 支付渠道格式化
     */
    fun getChannelFormat(): String {
        return when (channel) {
            Constant.ALIPAY -> "支付宝"
            Constant.WECHAT -> "微信"
            Constant.HUABEI_STAGE -> "花呗分期"
            else -> ""
        }
    }

    /**
     * 支付来源格式化
     */
    fun getOriginFormat(): String {
        return sourceFormat(origin)
    }

    /**
     * 来源格式化
     */
    private fun sourceFormat(data: Int): String {
        return when (data) {
            Constant.ANDROID -> "Android"
            Constant.IOS -> "iOS"
            Constant.WEB -> "Web"
            else -> ""
        }
    }

    /**
     * 订单来源格式化
     */
    fun getSourceFormat(): String {
        return sourceFormat(source)
    }
    //endregion
}
