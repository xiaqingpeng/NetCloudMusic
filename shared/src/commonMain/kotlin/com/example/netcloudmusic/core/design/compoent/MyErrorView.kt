package com.example.netcloudmusic.core.design.compoent

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** 兼容参考工程中的包名拼写。 */
@Composable
fun MyErrorView(
    throwable: Throwable? = null,
    message: String? = null,
    retryButtonText: String = "点击重试",
    icon: org.jetbrains.compose.resources.DrawableResource? = null,
    onRetryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    com.example.netcloudmusic.core.design.component.MyErrorView(
        throwable = throwable,
        message = message,
        retryButtonText = retryButtonText,
        icon = icon,
        onRetryClick = onRetryClick,
        modifier = modifier,
    )
}
