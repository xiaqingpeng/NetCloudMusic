package com.example.netcloudmusic.feature.cart.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.model.Cart
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.cart
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    datum: List<Cart> = emptyList(),
    isCheckAll: Boolean = false,
    isEditing: Boolean = false,
    selectedCount: Int = 0,
    price: Int = 0,
    shouldDisplayUndoRemove: Boolean = false,
    toProductDetail: (String) -> Unit = {},
    itemCheckChanged: (Cart) -> Unit = {},
    onDecrementCount: (Cart) -> Unit = {},
    onIncrementCount: (Cart) -> Unit = {},
    checkAllChanged: () -> Unit = {},
    onDeleteAllClick: () -> Unit = {},
    toConfirmOrder: () -> Unit = {},
    toggleEditingModel: () -> Unit = {},
    undoCartRemoval: () -> Unit = {},
    confirmDeleteByIds: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val bookmarkRemovedMessage = "已删除"
    val undoText = "撤销"

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            MyCenterTopAppBar(
                title = stringResource(Res.string.cart),
                actions = {
                    TextButton(onClick = toggleEditingModel) {
                        Text(
                            text = if (isEditing) "完成" else "编辑",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
        },
        bottomBar = {
            CartBottomBar(
                isCheckAll = isCheckAll,
                isEditing = isEditing,
                selectedCount = selectedCount,
                price = price,
                checkAllChanged = checkAllChanged,
                onDeleteAllClick = onDeleteAllClick,
                toConfirmOrder = toConfirmOrder,
            )
        },
        // 排除底部导航栏边距，防止 BottomBar 下方出现多余空间
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.navigationBars),
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(SpaceExtraMedium),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            items(datum, key = { it.id!! }) { cart ->
                CartItem(
                    data = cart,
                    toProductDetail = toProductDetail,
                    itemCheckChanged = itemCheckChanged,
                    onDecrementCount = onDecrementCount,
                    onIncrementCount = onIncrementCount,
                )
            }
        }
    }

    // 删除后显示撤销 Snackbar；
    // ActionPerformed = 用户点了"撤销" → undoCartRemoval（恢复本地列表）
    // Dismissed = Snackbar 自动消失 → confirmDeleteByIds（真正发起删除请求）
    LaunchedEffect(shouldDisplayUndoRemove) {
        if (shouldDisplayUndoRemove) {
            val actionPerformed = snackbarHostState.showSnackbar(
                message = bookmarkRemovedMessage,
                actionLabel = undoText,
                duration = SnackbarDuration.Short,
            ) == SnackbarResult.ActionPerformed

            if (actionPerformed) undoCartRemoval() else confirmDeleteByIds()
        }
    }
}
