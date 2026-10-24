package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 预览订单响应（v1/orders/confirm）
 */
@Serializable
data class ConfirmOrderResponse(
    /** 商品总价（分） */
    val totalPrice: Int = 0,

    /** 实付金额（分），减优惠券/运费后 */
    val price: Int = 0,

    /** 默认收货地址（可为 null，表示用户没有地址） */
    val address: Address? = null,

    /** 订单商品列表 */
    val carts: List<Cart> = emptyList(),
) {
    val totalPriceFloat get() = totalPrice * 1.0 / 100
    val priceFloat get() = price * 1.0 / 100
}
