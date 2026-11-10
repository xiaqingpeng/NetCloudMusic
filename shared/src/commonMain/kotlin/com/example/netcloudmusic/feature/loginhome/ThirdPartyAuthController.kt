package com.example.netcloudmusic.feature.loginhome

import androidx.compose.runtime.Composable

/**
 * 平台第三方登录控制器。
 *
 * commonMain UI 只通过该接口触发授权；Android 实际调浏览器 OAuth，
 * iOS 可给暂不支持提示。
 */
interface ThirdPartyAuthController {
    fun loginWithGithub()
}

@Composable
expect fun rememberThirdPartyAuthController(
    viewModel: LoginHomeViewModel,
): ThirdPartyAuthController
