package com.example.netcloudmusic.feature.loginhome

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberThirdPartyAuthController(
    viewModel: LoginHomeViewModel,
): ThirdPartyAuthController {
    val context = LocalContext.current

    DisposableEffect(viewModel) {
        ThirdPartyAuthBridge.onGithubCode = viewModel::onGithubCode
        ThirdPartyAuthBridge.onGithubError = { error ->
            viewModel.onThirdPartyError("GitHub 登录失败，$error")
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                val code = intent.getStringExtra(ThirdPartyAuthBridge.EXTRA_CODE)
                val error = intent.getStringExtra(ThirdPartyAuthBridge.EXTRA_ERROR)
                when {
                    code != null -> viewModel.onGithubCode(code)
                    error != null -> viewModel.onThirdPartyError("GitHub 登录失败，$error")
                }
            }
        }
        val filter = IntentFilter(ThirdPartyAuthBridge.ACTION_GITHUB_OAUTH_CALLBACK)
        context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)

        onDispose {
            context.unregisterReceiver(receiver)
            ThirdPartyAuthBridge.onGithubCode = null
            ThirdPartyAuthBridge.onGithubError = null
        }
    }

    return remember(viewModel) {
        object : ThirdPartyAuthController {
            override fun loginWithGithub() {
                ThirdPartyAuthBridge.startGithub?.invoke()
                    ?: viewModel.onThirdPartyError("GitHub 登录未初始化")
            }
        }
    }
}
