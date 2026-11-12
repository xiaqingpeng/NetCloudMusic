package com.example.netcloudmusic.feature.guide.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.guide.GuideRoute
import com.example.netcloudmusic.feature.splash.navigation.SPLASH_ROUTE
import com.example.netcloudmusic.ui.navigation.myComposable

const val GUIDE_ROUTE = "guide"

/**
 * 跳转到引导页，并清除启动页
 */
fun NavController.navigateToGuide() {
    navigate(GUIDE_ROUTE) {
        launchSingleTop = true
        popUpTo(SPLASH_ROUTE) {
            inclusive = true
        }
    }
}

/**
 * 配置引导页导航
 */
fun NavGraphBuilder.guideScreen(
    toMain: () -> Unit,
    toLogin: () -> Unit,
) {
    myComposable(route = GUIDE_ROUTE) {
        GuideRoute(
            toMain = toMain,
            toLogin = toLogin,
        )
    }
}
