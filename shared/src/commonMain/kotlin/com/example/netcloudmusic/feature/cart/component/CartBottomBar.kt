package com.example.netcloudmusic.feature.cart.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.theme.SpaceMediumWidth
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.design.theme.SpaceSmallWidth
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.price
import netcloudmusic.shared.generated.resources.radio_button
import netcloudmusic.shared.generated.resources.radio_button_checked
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 购物车底部操作栏
 *
 * - 编辑模式：全选 + 删除按钮
 * - 结算模式：全选 + 合计金额 + 结算按钮
 */
@Composable
fun CartBottomBar(
    isCheckAll: Boolean = false,
    isEditing: Boolean = false,
    selectedCount: Int = 0,
    price: Int = 0,
    checkAllChanged: () -> Unit = {},
    onDeleteAllClick: () -> Unit = {},
    toConfirmOrder: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = SpaceOuter),
    ) {
        // 全选按钮
        Image(
            painter = painterResource(
                if (isCheckAll) Res.drawable.radio_button_checked else Res.drawable.radio_button
            ),
            contentDescription = null,
            modifier = Modifier.clickable { checkAllChanged() },
        )

        SpaceSmallWidth()

        Text(
            text = "全选",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        SpaceMediumWidth()

        if (isEditing) {
            // 编辑模式：右侧显示删除按钮
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                onClick = onDeleteAllClick,
                enabled = selectedCount > 0,
                modifier = Modifier.widthIn(min = 120.dp),
            ) {
                Text(text = "删除")
            }
        } else {
            // 结算模式：合计金额 + 结算按钮
            Text(
                text = "合计",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(Res.string.price, price / 100.0),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
            )
            Button(
                onClick = toConfirmOrder,
                enabled = selectedCount > 0,
                modifier = Modifier.widthIn(min = 120.dp),
            ) {
                Text(
                    text = if (selectedCount == 0)
                        "结算"
                    else
                        "结算($selectedCount)",
                )
            }
        }
    }
}
