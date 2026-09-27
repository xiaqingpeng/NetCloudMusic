package com.example.netcloudmusic.feature.cart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.cart.CartRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val CART_ROUTE = "cart"

/**
 * 跳转到购物车界面
 */
fun NavController.navigateToCart() {
    navigate(CART_ROUTE) {
        launchSingleTop = true
    }
}

/**
 * 配置导航
 */
fun NavGraphBuilder.cartScreen() {
    myComposable(route = CART_ROUTE) {
        CartRoute()
    }
}
