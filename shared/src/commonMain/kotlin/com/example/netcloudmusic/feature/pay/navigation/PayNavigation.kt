package com.example.netcloudmusic.feature.pay.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.util.Constant
import com.example.netcloudmusic.feature.pay.PayRoute
import com.example.netcloudmusic.feature.pay.PayViewModel
import com.example.netcloudmusic.ui.navigation.myComposable

const val PAY_ROUTE = "pay_order"
const val ORDER_ID = "order_id"
const val STYLE = "style"

/**
 * 跳转到支付界面
 *
 * @param orderId 订单id
 * @param style 来源，0:确认订单，10:订单详情
 */
fun NavController.navigateToPay(orderId: String, style: Int = Constant.VALUE0) {
    navigate("${PAY_ROUTE}/$orderId/${style}") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.payScreen(
    toBack: () -> Unit,
    toOrderDetail: (orderId: String) -> Unit,
) {
    myComposable(
        "${PAY_ROUTE}/{${ORDER_ID}}/{${STYLE}}",
        arguments = listOf(
            navArgument(ORDER_ID) { type = NavType.StringType },
            navArgument(STYLE) { type = NavType.IntType },
        )
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments
            ?.read { getStringOrNull(ORDER_ID) }
            ?: ""
        val style = backStackEntry.arguments
            ?.read { getIntOrNull(STYLE) }
            ?: Constant.VALUE0
        
        val viewModel = PayViewModel(
            orderRepository = AppContainerProvider.instance.orderRepository,
            orderId = orderId,
            style = style,
        )
        
        PayRoute(
            toBack = toBack,
            toOrderDetail = toOrderDetail,
            viewModel = viewModel,
        )
    }
}
