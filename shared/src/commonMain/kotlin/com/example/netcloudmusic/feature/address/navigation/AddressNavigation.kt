package com.example.netcloudmusic.feature.address.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.address.AddressRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val ADDRESS_ROUTE = "address"

fun NavController.navigateToAddress() = navigate(ADDRESS_ROUTE)

/**
 * needRefresh：外部（MyApp）在地址保存成功后将此置 true，触发列表刷新。
 * 每次进入 addressScreen 的 content lambda 时，由调用方负责传入正确的值。
 */
fun NavGraphBuilder.addressScreen(
    toBack: () -> Unit,
    toAddAddress: () -> Unit,
    toAddressDetail: (String) -> Unit,
) {
    myComposable(
        route = ADDRESS_ROUTE,
        enterTransition = { slideInHorizontally(tween(300)) { it } },
        exitTransition = { slideOutHorizontally(tween(300)) { -it } },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it } },
        popExitTransition = { slideOutHorizontally(tween(300)) { it } },
    ) {
        AddressRoute(
            toBack = toBack,
            toAddAddress = toAddAddress,
            toAddressDetail = toAddressDetail,
        )
    }
}
