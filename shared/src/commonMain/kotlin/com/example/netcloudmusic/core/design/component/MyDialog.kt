package com.example.netcloudmusic.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.netcloudmusic.core.design.theme.Space3XLarge
import com.example.netcloudmusic.core.design.theme.SpaceExtraSmall
import com.example.netcloudmusic.core.design.theme.SpaceLarge
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.confirm
import org.jetbrains.compose.resources.stringResource

/**
 * 全局通用对话框
 *
 * 支持两种模式：
 * - 单按钮：仅传 [confirmTitle]，不传 [cancelTitle]（或设为 null），底部只显示"确认"
 * - 双按钮：同时传 [cancelTitle]，底部显示"取消 | 确认"
 *
 * @param title       标题文本
 * @param message     正文内容，可选。为 null 时不显示正文区域
 * @param confirmTitle 确认按钮文字，默认"确认"
 * @param cancelTitle  取消按钮文字，为 null 时隐藏取消按钮（单按钮模式）
 * @param onConfirm   确认回调
 * @param onCancel    取消回调，仅双按钮模式使用
 * @param onDismissRequest 点击遮罩关闭回调
 */
@Composable
fun MyDialog(
    title: String,
    message: String? = null,
    confirmTitle: String = stringResource(Res.string.confirm),
    cancelTitle: String? = null,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                // 标题
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = SpaceLarge)
                        .padding(
                            top = Space3XLarge,
                            bottom = if (message != null) SpaceMedium else Space3XLarge,
                        ),
                )

                // 正文（可选）
                if (message != null) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = SpaceLarge)
                            .padding(bottom = Space3XLarge),
                    )
                }

                // 分割线
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SpaceExtraSmall)
                        .background(MaterialTheme.colorScheme.outlineVariant),
                )

                // 按钮区
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                ) {
                    if (cancelTitle != null) {
                        // 取消按钮
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { onCancel() },
                        ) {
                            Text(
                                text = cancelTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }

                        // 垂直分割线
                        Spacer(
                            modifier = Modifier
                                .width(SpaceExtraSmall)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.outlineVariant),
                        )
                    }

                    // 确认按钮
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onConfirm() },
                    ) {
                        Text(
                            text = confirmTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
