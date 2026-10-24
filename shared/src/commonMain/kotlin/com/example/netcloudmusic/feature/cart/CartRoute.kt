package com.example.netcloudmusic.feature.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.feature.cart.component.CartScreen
import org.jetbrains.compose.resources.stringResource

/**
 * 购物车界面入口
 */
@Composable
fun CartRoute(
    toProductDetail: (String) -> Unit = {},
    toConfirmOrder: (List<String>) -> Unit = {},
) {
    val viewModel: CartViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                CartViewModel(
                    cartRepository = AppContainerProvider.instance.cartRepository
                )
            }
        }
    )

    val datum by viewModel.datum.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isCheckAll by viewModel.isCheckAll.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    val selectedCount by viewModel.selectedCount.collectAsState()
    val price by viewModel.price.collectAsState()

    // 首次进入时加载购物车数据
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    if (isLoading) {
        // 加载中状态
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text("加载中...")
        }
    } else if (datum.isEmpty()) {
        // 空购物车提示
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text("购物车为空")
        }
    } else {
        CartScreen(
            datum = datum,
            isCheckAll = isCheckAll,
            isEditing = isEditing,
            selectedCount = selectedCount,
            price = price,
            shouldDisplayUndoRemove = viewModel.shouldDisplayUndoRemove,
            toProductDetail = toProductDetail,
            itemCheckChanged = viewModel::itemCheckChanged,
            onDecrementCount = viewModel::onDecrementCount,
            onIncrementCount = viewModel::onIncrementCount,
            checkAllChanged = viewModel::checkAllChanged,
            onDeleteAllClick = viewModel::onDeleteAllClick,
            toConfirmOrder = { toConfirmOrder(viewModel.selectedCartIds()) },
            toggleEditingModel = viewModel::toggleEditingModel,
            undoCartRemoval = viewModel::undoCartRemoval,
            confirmDeleteByIds = viewModel::confirmDeleteByIds,
        )
    }
}

