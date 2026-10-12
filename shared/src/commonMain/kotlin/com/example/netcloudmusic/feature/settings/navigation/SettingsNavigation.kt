package com.example.netcloudmusic.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.settings.SettingsRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings() {
    navigate(SETTINGS_ROUTE)
}

fun NavGraphBuilder.settingsScreen(toBack: () -> Unit) {
    myComposable(route = SETTINGS_ROUTE) {
        SettingsRoute(toBack = toBack)
    }
}
