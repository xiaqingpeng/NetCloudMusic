package com.example.netcloudmusic.core.model

import com.example.netcloudmusic.core.util.Constant
import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    var productId: String? = null,
    var stockId: String? = null,
    var count: Int = 1,

    /** 从购物车购买时传入购物车 item id 列表 */
    var carts: List<String>? = null,

    /** 收货地址 id */
    var addressId: String? = null,

    /** 优惠券 id */
    var couponId: String? = null,

    /** 创建订单的平台，默认 Android */
    val source: Int = Constant.ANDROID,
)
