package com.example.netcloudmusic.core.design.component

import androidx.compose.runtime.Composable
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.cancel
import netcloudmusic.shared.generated.resources.confirm
import netcloudmusic.shared.generated.resources.confirm_delete
import org.jetbrains.compose.resources.stringResource

/**
 * 双按钮确认对话框（取消 / 确认）
 *
 * 基于 [MyDialog] 封装，保持原有调用接口不变。
 */
@Composable
fun MyConfirmDialog(
    title: String = stringResource(Res.string.confirm_delete),
    confirmTitle: String = stringResource(Res.string.confirm),
    cancelTitle: String = stringResource(Res.string.cancel),
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    MyDialog(
        title = title,
        confirmTitle = confirmTitle,
        cancelTitle = cancelTitle,
        onConfirm = onConfirm,
        onCancel = onCancel,
        onDismissRequest = onDismissRequest,
    )
}
