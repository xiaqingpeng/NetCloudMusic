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
import com.example.netcloudmusic.core.data.repository.ThemeMode
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.network.Network
import com.example.netcloudmusic.feature.about.navigation.aboutScreen
import com.example.netcloudmusic.feature.about.navigation.navigateToAbout
import com.example.netcloudmusic.feature.address.AddressRefreshBus
import com.example.netcloudmusic.feature.address.navigation.addressScreen
import com.example.netcloudmusic.feature.address.navigation.navigateToAddress
import com.example.netcloudmusic.feature.addressdetail.navigation.addressDetailScreen
import com.example.netcloudmusic.feature.addressdetail.navigation.navigateToAddAddress
import com.example.netcloudmusic.feature.addressdetail.navigation.navigateToAddressDetail
import com.example.netcloudmusic.feature.cart.navigation.cartScreen
import com.example.netcloudmusic.feature.cart.navigation.navigateToCart
import com.example.netcloudmusic.feature.codelogin.navigation.codeLoginScreen
import com.example.netcloudmusic.feature.codelogin.navigation.navigateToCodeLogin
import com.example.netcloudmusic.feature.guide.navigation.guideScreen
import com.example.netcloudmusic.feature.guide.navigation.navigateToGuide
import com.example.netcloudmusic.feature.login.navigation.finishLogin
import com.example.netcloudmusic.feature.login.navigation.loginScreen
import com.example.netcloudmusic.feature.login.navigation.navigateToLogin
import com.example.netcloudmusic.feature.loginhome.navigation.loginHomeScreen
import com.example.netcloudmusic.feature.loginhome.navigation.navigateToLoginHome
import com.example.netcloudmusic.feature.main.navigation.mainScreen
import com.example.netcloudmusic.feature.main.navigation.navigateToMain
import com.example.netcloudmusic.feature.productdetail.navigation.navigateToProductDetail
import com.example.netcloudmusic.feature.productdetail.navigation.productDetailScreen
import com.example.netcloudmusic.feature.profile.navigation.navigateToProfile
import com.example.netcloudmusic.feature.profile.navigation.profileScreen
import com.example.netcloudmusic.feature.register.navigation.navigateToRegister
import com.example.netcloudmusic.feature.register.navigation.registerScreen
import com.example.netcloudmusic.feature.selectcity.SelectCityCallback
import com.example.netcloudmusic.feature.selectcity.navigation.navigateToSelectCity
import com.example.netcloudmusic.feature.selectcity.navigation.selectCityScreen
import com.example.netcloudmusic.feature.settings.navigation.navigateToSettings
import com.example.netcloudmusic.feature.settings.navigation.settingsScreen
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
            popExitTransition = { ExitTransition.None },
        ) {
            // ── Splash / Guide ────────────────────────────────────────────────
            splashScreen(
                toGuide = navController::navigateToGuide,
                toMain = navController::navigateToMain,
            )
            guideScreen(
                toBack = navController::popBackStack,
                toMain = navController::navigateToMain,
            )

            // ── 主界面（底部 Tab）────────────────────────────────────────────
            mainScreen(
                toProductDetail = navController::navigateToProductDetail,
                toLogin = navController::navigateToLoginHome,
                toSettings = navController::navigateToSettings,
                toProfile = navController::navigateToProfile,
                toReceivingAddress = navController::navigateToAddress,
                toAbout = navController::navigateToAbout,
            )

            // ── 登录流程 ──────────────────────────────────────────────────────
            loginHomeScreen(
                toBack = { navController.popBackStack() },
                toLogin = navController::navigateToLogin,
                toCodeLogin = navController::navigateToCodeLogin,
            )
            loginScreen(
                toBack = { navController.popBackStack() },
                finishAllLoginPages = navController::finishLogin,
                toRegister = navController::navigateToRegister,
            )
            registerScreen(
                toBack = { navController.popBackStack() },
                finishAllLoginPages = navController::finishLogin,
            )
            codeLoginScreen(
                toBack = { navController.popBackStack() },
                finishAllLoginPages = navController::finishLogin,
            )

            // ── 用户模块 ──────────────────────────────────────────────────────
            profileScreen(
                toBack = { navController.popBackStack() },
                toSelectCity = { onCityResult ->
                    SelectCityCallback.onResult = onCityResult
                    navController.navigateToSelectCity()
                },
            )

            // ── 收货地址模块 ───────────────────────────────────────────────────
            addressScreen(
                toBack = { navController.popBackStack() },
                toAddAddress = navController::navigateToAddAddress,
                toAddressDetail = navController::navigateToAddressDetail,
            )
            addressDetailScreen(
                toBack = { navController.popBackStack() },
                // toSelectCity 接收一个 onCityResult 回调：
                // 1. 注册到 SelectCityCallback 单例
                // 2. 导航到选城市页
                // 3. 选城市页选完后触发该 callback，把省市区写回 ViewModel
                toSelectCity = { onCityResult ->
                    SelectCityCallback.onResult = onCityResult
                    navController.navigateToSelectCity()
                },
                onSaveSuccess = { AddressRefreshBus.trigger() },
            )
            selectCityScreen(
                toBack = { navController.popBackStack() },
            )

            // ── 关于 ──────────────────────────────────────────────────────────
            aboutScreen(toBack = { navController.popBackStack() })

            // ── 商品 ──────────────────────────────────────────────────────────
            productDetailScreen(
                toBack = { navController.popBackStack() },
                onCartClick = navController::navigateToCart,
            )
            cartScreen()

            // ── 设置 ──────────────────────────────────────────────────────────
            settingsScreen(toBack = { navController.popBackStack() })
        }
    }
}
