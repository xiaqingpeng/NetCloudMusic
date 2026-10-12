package com.example.netcloudmusic.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.login.LoginRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val LOGIN_ROUTE = "login"

fun NavController.navigateToLogin() {
    navigate(LOGIN_ROUTE) { launchSingleTop = true }
}

fun NavController.finishLogin() {
    popBackStack(LOGIN_ROUTE, inclusive = true)
}

fun NavGraphBuilder.loginScreen(
    toBack: () -> Unit,
    finishAllLoginPages: () -> Unit,
) {
    myComposable(LOGIN_ROUTE) {
        LoginRoute(toBack = toBack, finishAllLoginPages = finishAllLoginPages)
    }
}
