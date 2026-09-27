package com.example.netcloudmusic.feature.discovery.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.discovery.DiscoveryRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val DISCOVERY_ROUTE = "discover"

/**
 * 跳转到发现界面
 */
fun NavController.navigateToDiscover() {
    navigate(DISCOVERY_ROUTE) {
        launchSingleTop = true
    }
}

/**
 * 配置导航
 */
fun NavGraphBuilder.discoverScreen() {
    myComposable(route = DISCOVERY_ROUTE) {
        DiscoveryRoute()
    }
}
