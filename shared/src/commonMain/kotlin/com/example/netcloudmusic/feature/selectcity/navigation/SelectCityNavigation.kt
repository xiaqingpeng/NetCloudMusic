package com.example.netcloudmusic.feature.selectcity.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.netcloudmusic.feature.selectcity.SelectCityRoute
import kotlinx.serialization.Serializable

@Serializable
object SelectCityArgs

fun NavController.navigateToSelectCity() = navigate(SelectCityArgs)

fun NavGraphBuilder.selectCityScreen(toBack: () -> Unit) {
    composable<SelectCityArgs>(
        enterTransition = { slideInHorizontally(tween(300)) { it } },
        exitTransition = { slideOutHorizontally(tween(300)) { -it } },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it } },
        popExitTransition = { slideOutHorizontally(tween(300)) { it } },
    ) {
        SelectCityRoute(toBack = toBack)
    }
}
