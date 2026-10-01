package com.example.netcloudmusic.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.example.netcloudmusic.core.network.Network
import com.example.netcloudmusic.feature.guide.navigation.guideScreen
import com.example.netcloudmusic.feature.guide.navigation.navigateToGuide
import com.example.netcloudmusic.feature.main.navigation.mainScreen
import com.example.netcloudmusic.feature.main.navigation.navigateToMain
import com.example.netcloudmusic.feature.splash.navigation.SPLASH_ROUTE
import com.example.netcloudmusic.feature.splash.navigation.splashScreen
import com.example.netcloudmusic.ui.theme.AppTheme

@Composable
fun MyApp() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory(httpClient = { Network.httpClient }))
            }
            .build()
    }

    AppTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SPLASH_ROUTE,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            splashScreen(
                toGuide = navController::navigateToGuide,
                toMain = navController::navigateToMain
            )

            guideScreen(
                toBack = navController::popBackStack,
                toMain = navController::navigateToMain
            )

            mainScreen()
        }
    }
}
