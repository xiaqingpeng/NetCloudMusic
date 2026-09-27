package com.example.netcloudmusic.feature.shortvideo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.shortvideo.ShortVideoRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val SHORT_VIDEO_ROUTE = "short_video"

/**
 * 跳转到短视频界面
 */
fun NavController.navigateToShortVideo() {
    navigate(SHORT_VIDEO_ROUTE) {
        launchSingleTop = true
    }
}

/**
 * 配置导航
 */
fun NavGraphBuilder.shortVideoScreen() {
    myComposable(route = SHORT_VIDEO_ROUTE) {
        ShortVideoRoute()
    }
}
