package com.example.netcloudmusic.feature.loginhome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberThirdPartyAuthController(
    viewModel: LoginHomeViewModel,
): ThirdPartyAuthController {
    return remember(viewModel) {
        object : ThirdPartyAuthController {
            override fun loginWithGithub() {
                viewModel.onThirdPartyError("当前平台暂不支持该登录方式")
            }
        }
    }
}
