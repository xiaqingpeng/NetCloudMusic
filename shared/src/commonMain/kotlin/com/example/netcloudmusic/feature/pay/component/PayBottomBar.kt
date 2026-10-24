package com.example.netcloudmusic.feature.pay.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.go_pay
import org.jetbrains.compose.resources.stringResource

/**
 * 支付页面底部栏
 *
 * @param onPrimaryClick 去支付按钮点击回调
 */
@Composable
fun PayBottomBar(
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Button(
            onClick = onPrimaryClick,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(Res.string.go_pay))
        }
    }
}
