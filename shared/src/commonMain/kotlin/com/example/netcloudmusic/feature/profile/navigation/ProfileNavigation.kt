package com.example.netcloudmusic.feature.profile.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.profile.ProfileRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val PROFILE_ROUTE = "profile"

fun NavController.navigateToProfile() = navigate(PROFILE_ROUTE)

fun NavGraphBuilder.profileScreen(
    toBack: () -> Unit,
    toSelectCity: (onCityResult: (String, String, String, String, String, String) -> Unit) -> Unit,
) {
    myComposable(
        route = PROFILE_ROUTE,
        enterTransition = { slideInHorizontally(tween(300)) { it } },
        exitTransition = { slideOutHorizontally(tween(300)) { -it } },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it } },
        popExitTransition = { slideOutHorizontally(tween(300)) { it } },
    ) {
        ProfileRoute(toBack = toBack, toSelectCity = toSelectCity)
    }
}
