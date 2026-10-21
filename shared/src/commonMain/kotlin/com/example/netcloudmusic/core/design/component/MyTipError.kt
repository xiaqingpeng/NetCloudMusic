package com.example.netcloudmusic.core.design.component

import androidx.compose.runtime.Composable

/**
 * 提示错误信息（Toast）
 *
 * 因 Toast 是平台相关 API，具体实现在各平台的 actual 中。
 */
@Composable
expect fun MyTipError(
    visible: Boolean,
    message: String,
)
