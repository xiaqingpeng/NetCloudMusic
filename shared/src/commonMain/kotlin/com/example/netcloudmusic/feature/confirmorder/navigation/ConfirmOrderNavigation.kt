package com.example.netcloudmusic.feature.confirmorder.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.feature.confirmorder.ConfirmOrderRoute
import com.example.netcloudmusic.feature.confirmorder.ConfirmOrderViewModel
import com.example.netcloudmusic.ui.navigation.myComposable
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val CONFIRM_ORDER_ROUTE = "confirm_order"
const val CONFIRM_ORDER_PARAM = "confirm_order_param"

@OptIn(ExperimentalEncodingApi::class)
fun NavController.navigateToConfirmOrder(data: ConfirmOrderParam) {
    val paramString = Json.encodeToString(data)
    // Query 参数同样使用 URL-safe Base64，避免标准 Base64 的 `+` 被解析为空格。
    val encoded = Base64.UrlSafe.encode(paramString.toByteArray())
    navigate("$CONFIRM_ORDER_ROUTE?$CONFIRM_ORDER_PARAM=$encoded")
}

@OptIn(ExperimentalEncodingApi::class)
fun NavGraphBuilder.confirmOrderScreen(
    toBack: () -> Unit,
    toSelectAddress: () -> Unit,
    toPayOrder: (orderId: String) -> Unit,
    navController: NavHostController,
) {
    myComposable(
        route = "$CONFIRM_ORDER_ROUTE?$CONFIRM_ORDER_PARAM={$CONFIRM_ORDER_PARAM}",
        arguments = listOf(
            navArgument(CONFIRM_ORDER_PARAM) {
                type = NavType.StringType
                nullable = false
            }
        )
    ) { backStackEntry ->
        val encoded = backStackEntry.arguments
            ?.read { getStringOrNull(CONFIRM_ORDER_PARAM) }
            ?: ""
        val decoded = Base64.UrlSafe.decode(encoded).decodeToString()
        val param = Json.decodeFromString<ConfirmOrderParam>(decoded)
        
        val viewModel = ConfirmOrderViewModel(
            orderRepository = AppContainerProvider.instance.orderRepository,
            initialParam = param,
        )

        ConfirmOrderRoute(
            toBack = toBack,
            toSelectAddress = toSelectAddress,
            toPayOrder = toPayOrder,
            viewModel = viewModel,
            navController = navController,
        )
    }
}

@Serializable
data class ConfirmOrderParam(
    val productId: String? = null,
    val addressId: String? = null,
    val stockId: String? = null,
    val count: Int = 1,
    val carts: List<String>? = null,
)
