package com.example.netcloudmusic.feature.me.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.me.MeRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val ME_ROUTE = "me"

/**
 * 跳转到我的界面
 */
fun NavController.navigateToMe() {
    navigate(ME_ROUTE) {
        launchSingleTop = true
    }
}

/**
 * 配置导航
 */
fun NavGraphBuilder.meScreen(
    toScan: () -> Unit = {},
    toCode: () -> Unit = {},
) {
    myComposable(route = ME_ROUTE) {
        MeRoute(toScan = toScan, toCode = toCode)
    }
}
