package com.example.netcloudmusic.feature.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.main.MainRoute
import com.example.netcloudmusic.feature.splash.navigation.SPLASH_ROUTE
import com.example.netcloudmusic.ui.navigation.myComposable

const val MAIN_ROUTE = "main"

/**
 * 跳转到主界面
 */
fun NavController.navigateToMain() {
    navigate(MAIN_ROUTE) {
        launchSingleTop = true
        popUpTo(SPLASH_ROUTE)
    }
}

/**
 * 配置导航
 */
fun NavGraphBuilder.mainScreen(
    toProductDetail: (String) -> Unit = {},
    toLogin: () -> Unit = {},
    toSettings: () -> Unit = {},
) {
    myComposable(route = MAIN_ROUTE) {
        MainRoute(toProductDetail, toLogin, toSettings)
    }
}
