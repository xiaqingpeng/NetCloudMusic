package com.example.netcloudmusic.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.example.netcloudmusic.core.network.Network
import com.example.netcloudmusic.core.data.repository.ThemeMode
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.feature.guide.navigation.guideScreen
import com.example.netcloudmusic.feature.guide.navigation.navigateToGuide
import com.example.netcloudmusic.feature.main.navigation.mainScreen
import com.example.netcloudmusic.feature.main.navigation.navigateToMain
import com.example.netcloudmusic.feature.login.navigation.finishLogin
import com.example.netcloudmusic.feature.login.navigation.loginScreen
import com.example.netcloudmusic.feature.login.navigation.navigateToLogin
import com.example.netcloudmusic.feature.loginhome.navigation.loginHomeScreen
import com.example.netcloudmusic.feature.loginhome.navigation.navigateToLoginHome
import com.example.netcloudmusic.feature.cart.navigation.cartScreen
import com.example.netcloudmusic.feature.cart.navigation.navigateToCart
import com.example.netcloudmusic.feature.productdetail.navigation.navigateToProductDetail
import com.example.netcloudmusic.feature.productdetail.navigation.productDetailScreen
import com.example.netcloudmusic.feature.splash.navigation.SPLASH_ROUTE
import com.example.netcloudmusic.feature.splash.navigation.splashScreen
import com.example.netcloudmusic.feature.settings.navigation.navigateToSettings
import com.example.netcloudmusic.feature.settings.navigation.settingsScreen
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

    val themeMode by AppContainerProvider.instance.themeRepository.themeMode.collectAsState(
        initial = ThemeMode.SYSTEM
    )

    AppTheme(themeMode = themeMode) {
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

            mainScreen(
                toProductDetail = navController::navigateToProductDetail,
                toLogin = navController::navigateToLoginHome,
                toSettings = navController::navigateToSettings,
            )
            loginHomeScreen(
                toBack = { navController.popBackStack() },
                toLogin = navController::navigateToLogin,
            )
            loginScreen(
                toBack = { navController.popBackStack() },
                finishAllLoginPages = navController::finishLogin,
            )
            productDetailScreen(
                toBack = { navController.popBackStack() },
                onCartClick = navController::navigateToCart,
            )
            cartScreen()
            settingsScreen(toBack = { navController.popBackStack() })
        }
    }
}
