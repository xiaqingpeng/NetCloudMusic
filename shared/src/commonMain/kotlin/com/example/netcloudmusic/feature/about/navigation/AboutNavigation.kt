package com.example.netcloudmusic.feature.about.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.about.AboutRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val ABOUT_ROUTE = "about"

fun NavController.navigateToAbout() = navigate(ABOUT_ROUTE)

fun NavGraphBuilder.aboutScreen(toBack: () -> Unit) {
    myComposable(route = ABOUT_ROUTE) {
        AboutRoute(toBack = toBack)
    }
}
