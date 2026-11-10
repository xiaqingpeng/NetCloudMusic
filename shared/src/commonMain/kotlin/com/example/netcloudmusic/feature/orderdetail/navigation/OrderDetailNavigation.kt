package com.example.netcloudmusic.feature.orderdetail.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.feature.orderdetail.OrderDetailRoute
import com.example.netcloudmusic.feature.orderdetail.OrderDetailViewModel
import com.example.netcloudmusic.ui.navigation.myComposable

const val ORDER_DETAIL_ROUTE = "order_detail"
private const val ORDER_DETAIL_ID_KEY = "order_id"

fun NavController.navigateToOrderDetail(orderId: String) =
    navigate("${ORDER_DETAIL_ROUTE}/$orderId")

fun NavGraphBuilder.orderDetailScreen(
    toBack: () -> Unit,
    toPayOrder: (orderId: String) -> Unit,
) {
    myComposable(
        route = "${ORDER_DETAIL_ROUTE}/{${ORDER_DETAIL_ID_KEY}}",
        arguments = listOf(
            navArgument(ORDER_DETAIL_ID_KEY) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments
            ?.read { getStringOrNull(ORDER_DETAIL_ID_KEY) }
            ?: ""

        val viewModel: OrderDetailViewModel = viewModel(
            factory = viewModelFactory {
                initializer {
                    OrderDetailViewModel(
                        orderId = orderId,
                        orderRepository = AppContainerProvider.instance.orderRepository,
                    )
                }
            },
        )

        OrderDetailRoute(
            viewModel = viewModel,
            toBack = toBack,
            toPayOrder = toPayOrder,
        )
    }
}
