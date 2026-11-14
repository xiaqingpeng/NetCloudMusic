package com.example.netcloudmusic.feature.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.main.MainRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val MAIN_ROUTE = "main"

fun NavController.navigateToMain() {
    navigate(MAIN_ROUTE) {
        launchSingleTop = true
        // 清空启动/引导等前置页，避免返回
        popUpTo(0) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.mainScreen(
    toProductDetail: (String) -> Unit = {},
    toCategoryDetail: (com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg) -> Unit = {},
    toLogin: () -> Unit = {},
    toGithubProfile: () -> Unit = {},
    toSettings: () -> Unit = {},
    toProfile: () -> Unit = {},
    toReceivingAddress: () -> Unit = {},
    toAbout: () -> Unit = {},
    toOrder: (Int) -> Unit = {},
    toScan: () -> Unit = {},
    toCode: () -> Unit = {},
    toConfirmOrder: (List<String>) -> Unit = {},
) {
    myComposable(route = MAIN_ROUTE) {
        MainRoute(
            toProductDetail = toProductDetail,
            toCategoryDetail = toCategoryDetail,
            toLogin = toLogin,
            toGithubProfile = toGithubProfile,
            toSettings = toSettings,
            toProfile = toProfile,
            toReceivingAddress = toReceivingAddress,
            toAbout = toAbout,
            toOrder = toOrder,
            toScan = toScan,
            toCode = toCode,
            toConfirmOrder = toConfirmOrder,
        )
    }
}
