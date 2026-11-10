package com.example.netcloudmusic.core.design.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 上拉加载更多的文字提示行
 *
 * @param title     提示文字；为 null 或空则显示"—— 我是有底线的 ——"
 * @param onRetryClick  点击重试回调；不为 null 时整行可点击
 */
@Composable
fun MyLoadingText(
    title: String? = null,
    modifier: Modifier = Modifier,
    onRetryClick: (() -> Unit)? = null,
) {
    val text = if (title.isNullOrBlank()) "—— 我是有底线的 ——" else title
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onRetryClick != null) Modifier.clickable { onRetryClick() }
                else Modifier,
            )
            .padding(vertical = 12.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}
