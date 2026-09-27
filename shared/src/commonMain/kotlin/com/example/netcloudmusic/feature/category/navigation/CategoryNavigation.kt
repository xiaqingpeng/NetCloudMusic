package com.example.netcloudmusic.feature.category.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.category.CategoryRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val CATEGORY_ROUTE = "category"

/**
 * 跳转到分类界面
 */
fun NavController.navigateToCategory() {
    navigate(CATEGORY_ROUTE) {
        launchSingleTop = true
    }
}

/**
 * 配置导航
 */
fun NavGraphBuilder.categoryScreen() {
    myComposable(route = CATEGORY_ROUTE) {
        CategoryRoute()
    }
}
