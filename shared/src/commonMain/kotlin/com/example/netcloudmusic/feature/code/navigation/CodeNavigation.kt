package com.example.netcloudmusic.feature.code.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.code.CodeRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val CODE_ROUTE = "code"

fun NavController.navigateToCode() = navigate(CODE_ROUTE)

fun NavGraphBuilder.codeScreen(toBack: () -> Unit) {
    myComposable(CODE_ROUTE) {
        CodeRoute(toBack = toBack)
    }
}
