package com.example.netcloudmusic.feature.order.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.util.Constant
import com.example.netcloudmusic.feature.order.OrderRoute
import com.example.netcloudmusic.feature.order.OrderViewModel
import com.example.netcloudmusic.ui.navigation.myComposable

const val ORDER_ROUTE = "order"
private const val ORDER_STYLE_KEY = "style"

fun NavController.navigateToOrder(style: Int = Constant.VALUE0) =
    navigate("${ORDER_ROUTE}/$style")

fun NavGraphBuilder.orderScreen(
    toBack: () -> Unit,
    toOrderDetail: (orderId: String) -> Unit,
    toPayOrder: (orderId: String) -> Unit,
) {
    myComposable(
        route = "${ORDER_ROUTE}/{${ORDER_STYLE_KEY}}",
        arguments = listOf(
            navArgument(ORDER_STYLE_KEY) { type = NavType.IntType },
        ),
    ) { backStackEntry ->
        val style = backStackEntry.arguments
            ?.read { getIntOrNull(ORDER_STYLE_KEY) }
            ?: Constant.VALUE0

        val viewModel: OrderViewModel = viewModel(
            factory = viewModelFactory {
                initializer {
                    OrderViewModel(
                        orderRepository = AppContainerProvider.instance.orderRepository,
                        initialSelectedIndex = style,
                    )
                }
            },
        )

        OrderRoute(
            viewModel = viewModel,
            toBack = toBack,
            toOrderDetail = toOrderDetail,
            toPayOrder = toPayOrder,
        )
    }
}
