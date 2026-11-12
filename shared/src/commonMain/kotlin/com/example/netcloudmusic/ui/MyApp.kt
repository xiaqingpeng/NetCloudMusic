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
import com.example.netcloudmusic.core.util.Constant
import com.example.netcloudmusic.feature.about.navigation.aboutScreen
import com.example.netcloudmusic.feature.about.navigation.navigateToAbout
import com.example.netcloudmusic.feature.address.AddressRefreshBus
import com.example.netcloudmusic.feature.address.navigation.addressScreen
import com.example.netcloudmusic.feature.address.navigation.navigateToAddress
import com.example.netcloudmusic.feature.addressdetail.navigation.addressDetailScreen
import com.example.netcloudmusic.feature.addressdetail.navigation.navigateToAddAddress
import com.example.netcloudmusic.feature.addressdetail.navigation.navigateToAddressDetail
import com.example.netcloudmusic.feature.categorydetail.navigation.categoryDetailScreen
import com.example.netcloudmusic.feature.categorydetail.navigation.navigateToCategoryDetail
import com.example.netcloudmusic.feature.codelogin.navigation.codeLoginScreen
import com.example.netcloudmusic.feature.codelogin.navigation.navigateToCodeLogin
import com.example.netcloudmusic.feature.confirmorder.navigation.ConfirmOrderParam
import com.example.netcloudmusic.feature.confirmorder.navigation.confirmOrderScreen
import com.example.netcloudmusic.feature.confirmorder.navigation.navigateToConfirmOrder
import com.example.netcloudmusic.feature.forgetpassword.navigation.forgetPasswordScreen
import com.example.netcloudmusic.feature.forgetpassword.navigation.navigateToForgetPassword
import com.example.netcloudmusic.feature.guide.navigation.guideScreen
import com.example.netcloudmusic.feature.guide.navigation.navigateToGuide
import com.example.netcloudmusic.feature.githubprofile.navigation.githubProfileScreen
import com.example.netcloudmusic.feature.githubprofile.navigation.navigateToGithubProfile
import com.example.netcloudmusic.feature.login.navigation.finishLogin
import com.example.netcloudmusic.feature.login.navigation.loginScreen
import com.example.netcloudmusic.feature.login.navigation.navigateToLogin
import com.example.netcloudmusic.feature.loginhome.navigation.loginHomeScreen
import com.example.netcloudmusic.feature.loginhome.navigation.navigateToLoginHome
import com.example.netcloudmusic.feature.main.navigation.mainScreen
import com.example.netcloudmusic.feature.main.navigation.navigateToMain
import com.example.netcloudmusic.feature.pay.navigation.navigateToPay
import com.example.netcloudmusic.feature.pay.navigation.payScreen
import com.example.netcloudmusic.feature.order.navigation.navigateToOrder
import com.example.netcloudmusic.feature.order.navigation.orderScreen
import com.example.netcloudmusic.feature.orderdetail.navigation.navigateToOrderDetail
import com.example.netcloudmusic.feature.orderdetail.navigation.orderDetailScreen
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
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.http.HttpHeaders

@Composable
fun MyApp() {
    setSingletonImageLoaderFactory { context ->
        // 图片请求不能复用业务 API client：API client 会自动携带登录
        // Authorization，部分第三方图片 CDN 会因此返回 403。
        val imageHttpClient = HttpClient {
            install(DefaultRequest) {
                headers.append(
                    HttpHeaders.UserAgent,
                    "Mozilla/5.0 (iPhone; CPU iPhone OS 18_0 like Mac OS X) " +
                        "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.0 " +
                        "Mobile/15E148 Safari/604.1"
                )
            }
        }
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory(httpClient = { imageHttpClient }))
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
                toMain = navController::navigateToMain,
                toLogin = {
                    // 先进入主页，再打开登录首页（与 Android Compose 版一致）
                    navController.navigateToMain()
                    navController.navigateToLoginHome()
                },
            )

            // ── 主界面（底部 Tab）────────────────────────────────────────────
            mainScreen(
                toProductDetail = navController::navigateToProductDetail,
                toCategoryDetail = navController::navigateToCategoryDetail,
                toLogin = navController::navigateToLoginHome,
                toGithubProfile = navController::navigateToGithubProfile,
                toSettings = navController::navigateToSettings,
                toProfile = navController::navigateToProfile,
                toReceivingAddress = navController::navigateToAddress,
                toAbout = navController::navigateToAbout,
                toOrder = { style -> navController.navigateToOrder(style) },
                toConfirmOrder = { cartIds ->
                    navController.navigateToConfirmOrder(
                        ConfirmOrderParam(carts = cartIds)
                    )
                },
            )

            // ── 登录流程 ──────────────────────────────────────────────────────
            loginHomeScreen(
                toBack = { navController.popBackStack() },
                toLogin = navController::navigateToLogin,
                toCodeLogin = navController::navigateToCodeLogin,
                finishAllLoginPages = navController::finishLogin,
            )
            loginScreen(
                toBack = { navController.popBackStack() },
                finishAllLoginPages = navController::finishLogin,
                toRegister = navController::navigateToRegister,
                toSetPassword = navController::navigateToForgetPassword,
            )
            registerScreen(
                toBack = { navController.popBackStack() },
                finishAllLoginPages = navController::finishLogin,
            )
            codeLoginScreen(
                toBack = { navController.popBackStack() },
                finishAllLoginPages = navController::finishLogin,
            )
            forgetPasswordScreen(
                toBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() },
            )

            // ── 用户模块 ──────────────────────────────────────────────────────
            profileScreen(
                toBack = { navController.popBackStack() },
                toSelectCity = { onCityResult ->
                    SelectCityCallback.onResult = onCityResult
                    navController.navigateToSelectCity()
                },
            )
            githubProfileScreen(
                toBack = { navController.popBackStack() },
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
                toConfirmOrder = { orderRequest ->
                    navController.navigateToConfirmOrder(
                        ConfirmOrderParam(
                            productId = orderRequest.productId,
                            stockId = orderRequest.stockId,
                            count = orderRequest.count,
                        )
                    )
                },
            )

            // ── 分类详情 ───────────────────────────────────────────────────────
            categoryDetailScreen(
                toBack = { navController.popBackStack() },
                toProductDetail = navController::navigateToProductDetail,
            )

            // ── 订单流程 ───────────────────────────────────────────────────────
            confirmOrderScreen(
                toBack = { navController.popBackStack() },
                toSelectAddress = navController::navigateToAddress,
                toPayOrder = { orderId -> navController.navigateToPay(orderId) },
                navController = navController,
            )
            payScreen(
                toBack = { navController.popBackStack() },
                toOrderDetail = { orderId ->
                    navController.navigateToOrderDetail(orderId)
                },
            )
            orderScreen(
                toBack = { navController.popBackStack() },
                toOrderDetail = { orderId -> navController.navigateToOrderDetail(orderId) },
                // style=10：从订单列表进入支付，支付完成后应返回而非再推一层详情
                toPayOrder = { orderId ->
                    navController.navigateToPay(orderId, style = Constant.VALUE10)
                },
            )
            orderDetailScreen(
                toBack = { navController.popBackStack() },
                toPayOrder = { orderId ->
                    navController.navigateToPay(orderId, style = Constant.VALUE10)
                },
            )

            // ── 设置 ──────────────────────────────────────────────────────────
            settingsScreen(
                toBack = { navController.popBackStack() },
                // 设置页的退出登录会进入登录流程。先移除设置页，
                // 否则登录成功后 finishLogin 只会弹出登录页，返回栈顶仍然是设置页。
                toLogin = {
                    navController.popBackStack()
                    navController.navigateToLoginHome()
                },
            )
        }
    }
}
