package com.example.netcloudmusic.feature.loginhome.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.loginhome.LoginHomeRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val LOGIN_HOME_ROUTE = "login_home"

fun NavController.navigateToLoginHome() {
    navigate(LOGIN_HOME_ROUTE) { launchSingleTop = true }
}

fun NavGraphBuilder.loginHomeScreen(
    toBack: () -> Unit,
    toLogin: () -> Unit = {},
    toCodeLogin: () -> Unit = {},
) {
    myComposable(
        route = LOGIN_HOME_ROUTE,
        enterTransition = { slideInHorizontally(tween(300)) { it } },
        exitTransition = { slideOutHorizontally(tween(300)) { -it } },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it } },
        popExitTransition = { slideOutHorizontally(tween(300)) { it } },
    ) {
        LoginHomeRoute(toBack, toLogin, toCodeLogin)
    }
}
