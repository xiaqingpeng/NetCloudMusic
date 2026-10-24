package com.example.netcloudmusic.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * 提示占位符组件
 * 用于显示成功和错误提示
 */
@Composable
fun MyTipPlaceholder(
    tipSuccessMessage: String?,
    clearTipSuccessMessage: () -> Unit,
    tipError: String?,
    clearTipError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // 显示成功提示
    LaunchedEffect(tipSuccessMessage) {
        tipSuccessMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            clearTipSuccessMessage()
        }
    }

    // 显示错误提示
    LaunchedEffect(tipError) {
        tipError?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            clearTipError()
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
    )
}
