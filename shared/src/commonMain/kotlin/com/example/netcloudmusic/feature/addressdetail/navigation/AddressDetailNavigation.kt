package com.example.netcloudmusic.feature.addressdetail.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.netcloudmusic.feature.addressdetail.AddressDetailRoute
import kotlinx.serialization.Serializable

@Serializable
data class AddressDetailArgs(
    /** 空字符串表示新增模式 */
    val addressId: String = "",
)

/** 新增地址 */
fun NavController.navigateToAddAddress() = navigate(AddressDetailArgs())

/** 编辑地址 */
fun NavController.navigateToAddressDetail(addressId: String) =
    navigate(AddressDetailArgs(addressId))

fun NavGraphBuilder.addressDetailScreen(
    toBack: () -> Unit,
    toSelectCity: (onCityResult: (province: String, provinceCode: String, city: String, cityCode: String, area: String, areaCode: String) -> Unit) -> Unit,
    onSaveSuccess: () -> Unit = {},
) {
    composable<AddressDetailArgs>(
        enterTransition = { slideInHorizontally(tween(300)) { it } },
        exitTransition = { slideOutHorizontally(tween(300)) { -it } },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it } },
        popExitTransition = { slideOutHorizontally(tween(300)) { it } },
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<AddressDetailArgs>()
        AddressDetailRoute(
            addressId = args.addressId,
            toBack = toBack,
            toSelectCity = toSelectCity,
            onSaveSuccess = onSaveSuccess,
        )
    }
}
