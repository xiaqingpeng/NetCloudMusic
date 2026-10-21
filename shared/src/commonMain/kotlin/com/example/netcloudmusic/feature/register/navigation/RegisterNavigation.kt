package com.example.netcloudmusic.feature.register.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.register.RegisterRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val REGISTER_ROUTE = "register"

fun NavController.navigateToRegister() = navigate(REGISTER_ROUTE)

fun NavGraphBuilder.registerScreen(
    toBack: () -> Unit,
    finishAllLoginPages: () -> Unit = {},
) {
    myComposable(
        route = REGISTER_ROUTE,
        enterTransition = { slideInHorizontally(tween(300)) { it } },
        exitTransition = { slideOutHorizontally(tween(300)) { -it } },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it } },
        popExitTransition = { slideOutHorizontally(tween(300)) { it } },
    ) {
        RegisterRoute(
            toBack = toBack,
            finishAllLoginPages = finishAllLoginPages,
        )
    }
}
