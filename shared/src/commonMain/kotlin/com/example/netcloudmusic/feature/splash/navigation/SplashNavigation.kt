package com.example.netcloudmusic.feature.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.splash.SplashRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val SPLASH_ROUTE = "splash"

/**
 * 跳转到这个界面
 */
fun NavController.navigateToSplash() {
    navigate(SPLASH_ROUTE)
}

/**
 * 配置导航
 */
fun NavGraphBuilder.splashScreen(
    toGuide: () -> Unit,
    toMain: () -> Unit
) {
    myComposable(route = SPLASH_ROUTE) {
        SplashRoute(
            toGuide = toGuide,
            toMain = toMain
        )
    }
}

