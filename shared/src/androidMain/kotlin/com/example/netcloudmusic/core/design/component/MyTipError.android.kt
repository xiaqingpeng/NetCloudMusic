package com.example.netcloudmusic.core.design.component

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Android 端实现：使用 Toast 提示错误信息
 */
@Composable
actual fun MyTipError(
    visible: Boolean,
    message: String,
) {
    val context = LocalContext.current
    if (visible && message.isNotEmpty()) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
