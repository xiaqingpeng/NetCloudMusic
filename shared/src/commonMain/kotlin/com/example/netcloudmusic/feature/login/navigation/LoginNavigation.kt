package com.example.netcloudmusic.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.login.LoginRoute
import com.example.netcloudmusic.feature.loginhome.navigation.LOGIN_HOME_ROUTE
import com.example.netcloudmusic.ui.navigation.myComposable

const val LOGIN_ROUTE = "login"

fun NavController.navigateToLogin() {
    navigate(LOGIN_ROUTE) { launchSingleTop = true }
}

/**
 * 关闭所有登录相关页面（loginHome / login / codeLogin / register），
 * 弹出到 loginHome 并把它也移除，回到主界面。
 */
fun NavController.finishLogin() {
    popBackStack(LOGIN_HOME_ROUTE, inclusive = true)
}

fun NavGraphBuilder.loginScreen(
    toBack: () -> Unit,
    finishAllLoginPages: () -> Unit,
    toRegister: () -> Unit = {},
) {
    myComposable(LOGIN_ROUTE) {
        LoginRoute(
            toBack = toBack,
            finishAllLoginPages = finishAllLoginPages,
            toRegister = toRegister,
        )
    }
}
