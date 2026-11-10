package com.example.netcloudmusic.feature.address.compoent

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.component.MyConfirmDialog
import com.example.netcloudmusic.core.model.Address
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private val ACTION_BTN_WIDTH = 80.dp
private val ACTION_TOTAL_WIDTH = ACTION_BTN_WIDTH * 2

/**
 * 带左滑操作的地址列表项（编辑 + 删除）
 */
@Composable
fun MyAddressItem(
    data: Address,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        MyConfirmDialog(
            onConfirm = { showDeleteDialog = false; onDeleteClick() },
            onDismissRequest = { showDeleteDialog = false },
        )
    }

    Layout(
        contents = listOf(
            // 内容
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onItemClick() }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                data.name ?: "",
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Text(
                                "  ${data.telephone}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline,
                            )
                            if (data.default) {
                                Text(
                                    " 默认",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(start = 8.dp),
                                )
                            }
                        }
                        Text(
                            data.fullAddress,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            },
            // 编辑按钮
            {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(ACTION_BTN_WIDTH)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onEditClick() },
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "编辑", tint = Color.White)
                }
            },
            // 删除按钮
            {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(ACTION_BTN_WIDTH)
                        .background(Color(0xFFFF4444))
                        .clickable { showDeleteDialog = true },
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "删除", tint = Color.White)
                }
            },
        ),
        modifier = modifier
            .clipToBounds()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        val target = if (offsetX.value < -ACTION_TOTAL_WIDTH.toPx() / 2)
                            -ACTION_TOTAL_WIDTH.toPx() else 0f
                        scope.launch { offsetX.animateTo(target, tween(200)) }
                    },
                ) { _, delta ->
                    val newVal = (offsetX.value + delta).coerceIn(-ACTION_TOTAL_WIDTH.toPx(), 0f)
                    scope.launch { offsetX.snapTo(newVal) }
                }
            },
    ) { (contentM, editM, deleteM), constraints ->
        val contentP = contentM.first().measure(constraints)
        val h = contentP.height
        val btnC = Constraints(minWidth = 0, maxWidth = Constraints.Infinity, minHeight = h, maxHeight = h)
        val editP = editM.first().measure(btnC)
        val deleteP = deleteM.first().measure(btnC)
        val totalW = contentP.width
        layout(totalW, h) {
            val ox = offsetX.value.roundToInt()
            contentP.placeRelative(ox, 0)
            editP.placeRelative(ox + totalW, 0)
            deleteP.placeRelative(ox + totalW + editP.width, 0)
        }
    }
}
