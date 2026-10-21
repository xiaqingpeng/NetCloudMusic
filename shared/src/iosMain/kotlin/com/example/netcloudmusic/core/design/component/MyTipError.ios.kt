package com.example.netcloudmusic.core.design.component

import androidx.compose.runtime.Composable

/**
 * iOS 端实现：暂不支持 Toast，静默忽略
 */
@Composable
actual fun MyTipError(
    visible: Boolean,
    message: String,
) {
    // iOS 不支持 Toast，可在此接入平台特定的提示 UI
}
