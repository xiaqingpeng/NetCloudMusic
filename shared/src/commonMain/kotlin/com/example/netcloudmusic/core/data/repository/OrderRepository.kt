package com.example.netcloudmusic.core.data.repository

import com.example.netcloudmusic.core.model.ConfirmOrderResponse
import com.example.netcloudmusic.core.model.Order
import com.example.netcloudmusic.core.model.OrderRequest
import com.example.netcloudmusic.core.model.PayRequest
import com.example.netcloudmusic.core.model.PayResponse
import com.example.netcloudmusic.core.model.response.NetworkResponse
import com.example.netcloudmusic.core.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 订单仓库，处理订单相关的网络请求
 */
class OrderRepository(
    private val dataSource: NetworkDataSource,
) {
    /**
     * 预览订单（确认订单页面）
     */
    fun confirmOrder(request: OrderRequest): Flow<NetworkResponse<ConfirmOrderResponse>> = flow {
        emit(dataSource.confirmOrder(request))
    }

    /**
     * 创建订单
     */
    fun createOrder(request: OrderRequest): Flow<NetworkResponse<Order>> = flow {
        emit(dataSource.createOrder(request))
    }

    /**
     * 订单详情
     */
    fun orderDetail(orderId: String): Flow<NetworkResponse<Order>> = flow {
        emit(dataSource.orderDetail(orderId))
    }

    /**
     * 请求支付
     */
    fun orderPay(request: PayRequest): Flow<NetworkResponse<PayResponse>> = flow {
        emit(dataSource.orderPay(request))
    }
}
