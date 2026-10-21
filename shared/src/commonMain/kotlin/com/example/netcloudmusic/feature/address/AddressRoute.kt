package com.example.netcloudmusic.feature.address

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.model.Address
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 收货地址列表页
 */
@Composable
fun AddressRoute(
    toBack: () -> Unit,
    toAddAddress: () -> Unit,
    toAddressDetail: (String) -> Unit,
) {
    val viewModel: AddressViewModel = viewModel(
        factory = viewModelFactory {
            initializer { AddressViewModel(AppContainerProvider.instance.addressRepository) }
        },
    )
    val uiState by viewModel.uiState.collectAsState()
    val toast by viewModel.toast.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Toast 提示（删除成功/失败等）
    LaunchedEffect(toast) {
        val msg = toast ?: return@LaunchedEffect
        viewModel.consumeToast()
        snackbarHostState.showSnackbar(msg)
    }

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                title = "收货地址",
                toBack = toBack,
                actions = {
                    IconButton(onClick = toAddAddress) {
                        Icon(Icons.Default.Add, contentDescription = "新增")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
    ) { contentPadding ->
        when (val state = uiState) {
            is AddressUiState.Success -> {
                if (state.datum.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(contentPadding),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "暂无收货地址，点击右上角 + 添加",
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(contentPadding),
                    ) {
                        items(state.datum, key = { it.id }) { address ->
                            AddressItem(
                                data = address,
                                onEditClick = { toAddressDetail(address.id) },
                                onDeleteClick = { viewModel.deleteAddress(address.id) },
                            )
                        }
                    }
                }
            }
            is AddressUiState.Error -> {
                LaunchedEffect(state) {
                    snackbarHostState.showSnackbar(state.message ?: "加载失败，请重试")
                }
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding))
            }
            else -> Box(modifier = Modifier.fillMaxSize().padding(contentPadding))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 左滑操作地址列表项
// ─────────────────────────────────────────────────────────────────────────────

private val ACTION_BUTTON_WIDTH = 80.dp

@Composable
fun AddressItem(
    data: Address,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("确认删除") },
            text = { Text("确认删除该收货地址？") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    scope.launch { offsetX.animateTo(0f, tween(200)) }
                    onDeleteClick()
                }) { Text("删除") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("取消") }
            },
        )
    }

    SwipeActionLayout(
        offsetXProvider = { offsetX.value },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                val maxPx = (ACTION_BUTTON_WIDTH * 2).toPx()
                detectHorizontalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (-offsetX.value > maxPx / 2f) offsetX.animateTo(-maxPx, tween(200))
                            else offsetX.animateTo(0f, tween(200))
                        }
                    },
                    onDragCancel = { scope.launch { offsetX.animateTo(0f, tween(200)) } },
                ) { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        offsetX.snapTo((offsetX.value + dragAmount).coerceIn(-maxPx, 0f))
                    }
                }
            },
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(data.getContact(), style = MaterialTheme.typography.bodyMedium)
                        if (data.default) {
                            Text(
                                "  默认",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    Text(
                        data.getReceiverArea(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                    Text(data.detail, style = MaterialTheme.typography.bodyLarge)
                }
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp),
                    tint = MaterialTheme.colorScheme.outline,
                )
            }
        },
        editAction = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(ACTION_BUTTON_WIDTH)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        scope.launch { offsetX.animateTo(0f, tween(200)) }
                        onEditClick()
                    },
            ) { Text("编辑", color = MaterialTheme.colorScheme.onSecondary) }
        },
        deleteAction = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(ACTION_BUTTON_WIDTH)
                    .background(MaterialTheme.colorScheme.error)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { showDeleteDialog = true },
            ) { Text("删除", color = MaterialTheme.colorScheme.onError) }
        },
    )
}

@Composable
private fun SwipeActionLayout(
    offsetXProvider: () -> Float,
    content: @Composable () -> Unit,
    editAction: @Composable () -> Unit,
    deleteAction: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Layout(
        contents = listOf(content, editAction, deleteAction),
        modifier = modifier.clipToBounds(),
    ) { (contentM, editM, deleteM), constraints ->
        val contentP = contentM.first().measure(constraints)
        val h = contentP.height
        val btnC = Constraints(minWidth = 0, maxWidth = Constraints.Infinity, minHeight = h, maxHeight = h)
        val editP = editM.first().measure(btnC)
        val deleteP = deleteM.first().measure(btnC)
        layout(contentP.width, h) {
            val dx = offsetXProvider().roundToInt()
            contentP.placeRelative(dx, 0)
            editP.placeRelative(contentP.width + dx, 0)
            deleteP.placeRelative(contentP.width + editP.width + dx, 0)
        }
    }
}
