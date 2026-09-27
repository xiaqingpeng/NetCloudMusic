package com.example.netcloudmusic.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.netcloudmusic.feature.guide.GuideScreen
import com.example.netcloudmusic.feature.splash.SplashRoute
import com.example.netcloudmusic.ui.theme.AppTheme

@Composable
fun MyApp() {
    AppTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") {
                SplashRoute(
                    toGuide = {
                        navController.navigate("guide")
                    }
                )
            }
            composable("guide") {
                GuideScreen(
                    toBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
