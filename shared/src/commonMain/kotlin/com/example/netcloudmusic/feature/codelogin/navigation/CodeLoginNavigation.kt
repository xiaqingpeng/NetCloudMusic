package com.example.netcloudmusic.feature.codelogin.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.codelogin.CodeLoginRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val CODE_LOGIN_ROUTE = "code_login"

fun NavController.navigateToCodeLogin() = navigate(CODE_LOGIN_ROUTE)

fun NavGraphBuilder.codeLoginScreen(
    toBack: () -> Unit,
    finishAllLoginPages: () -> Unit,
) {
    myComposable(
        route = CODE_LOGIN_ROUTE,
        enterTransition = { slideInHorizontally(tween(300)) { it } },
        exitTransition = { slideOutHorizontally(tween(300)) { -it } },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it } },
        popExitTransition = { slideOutHorizontally(tween(300)) { it } },
    ) {
        CodeLoginRoute(
            toBack = toBack,
            finishAllLoginPages = finishAllLoginPages,
        )
    }
}
