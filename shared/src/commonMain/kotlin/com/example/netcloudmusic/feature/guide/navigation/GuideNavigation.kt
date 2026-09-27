package com.example.netcloudmusic.feature.guide.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.guide.GuideRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val GUIDE_ROUTE = "guide"

/**
 * 跳转到这个界面
 */
fun NavController.navigateToGuide() {
    navigate(GUIDE_ROUTE)
}

/**
 * 配置导航
 */
fun NavGraphBuilder.guideScreen(
    toBack: () -> Unit,
    toMain: () -> Unit = {}
) {
    myComposable(route = GUIDE_ROUTE) {
        GuideRoute(
            toBack = toBack,
            toMain = toMain
        )
    }
}
