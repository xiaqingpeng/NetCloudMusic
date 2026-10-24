package com.example.netcloudmusic.core.util

import androidx.compose.runtime.Composable

/**
 * 系统分享能力。
 *
 * 分享面板属于平台 UI，因此只在 commonMain 定义能力，
 * Android/iOS 分别调用各自的系统分享 API。
 */
interface ShareLauncher {
    fun shareText(text: String)
    fun shareImage(data: ByteArray)
}

@Composable
expect fun rememberShareLauncher(): ShareLauncher
