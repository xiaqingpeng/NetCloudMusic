package com.example.netcloudmusic.feature.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.design.component.MyLoadMoreLoading
import com.example.netcloudmusic.core.design.component.MyLoadingText
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.design.theme.SpaceExtraMediumHeight
import com.example.netcloudmusic.core.extension.isScrollingToEnd
import com.example.netcloudmusic.core.model.Order
import com.example.netcloudmusic.feature.order.component.OrderItem
import com.example.netcloudmusic.feature.order.component.OrderTab
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.my_order
import org.jetbrains.compose.resources.stringResource

@Composable
fun OrderRoute(
    viewModel: OrderViewModel,
    toBack: () -> Unit,
    toOrderDetail: (String) -> Unit,
    toPayOrder: (String) -> Unit,
) {
    val datum by viewModel.datum.collectAsState()
    val selectedIndex by viewModel.selectedIndex.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()

    OrderScreen(
        datum = datum,
        selectedIndex = selectedIndex,
        loadingState = loadingState,
        toBack = toBack,
        loadMore = viewModel::loadMore,
        selectedIndexChanged = viewModel::selectedIndexChanged,
        toOrderDetail = toOrderDetail,
        toPayOrder = toPayOrder,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    datum: List<Order> = emptyList(),
    selectedIndex: Int = 0,
    loadingState: LoadingState = LoadingState.None,
    toBack: () -> Unit = {},
    loadMore: () -> Unit = {},
    selectedIndexChanged: (Int) -> Unit = {},
    toOrderDetail: (String) -> Unit = {},
    toPayOrder: (String) -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                toBack = toBack,
                title = stringResource(Res.string.my_order),
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val listState = rememberLazyListState()
        val isLoadMoreTriggered by remember {
            derivedStateOf { listState.isScrollingToEnd() }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            OrderTab(selectedIndex = selectedIndex, tabChanged = selectedIndexChanged)
            SpaceExtraMediumHeight()

            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(SpaceExtraMedium),
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) {
                if (datum.isEmpty() &&
                    loadingState !is LoadingState.Loading &&
                    loadingState !is LoadingState.Error
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 80.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "暂无订单",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                            )
                        }
                    }
                }
                items(datum) { order ->
                    OrderItem(
                        data = order,
                        modifier = Modifier.clickable { toOrderDetail(order.id) },
                        toPayOrder = toPayOrder,
                    )
                }
                item {
                    when (val s = loadingState) {
                        is LoadingState.Loading -> MyLoadMoreLoading()
                        is LoadingState.Error -> MyLoadingText(title = s.data, onRetryClick = loadMore)
                        is LoadingState.NoMoreData -> {
                            if (datum.isNotEmpty()) MyLoadingText()
                            else Spacer(Modifier.navigationBarsPadding())
                        }
                        is LoadingState.None -> Spacer(Modifier.navigationBarsPadding())
                    }
                }
            }
        }

        if (isLoadMoreTriggered) {
            LaunchedEffect(Unit) { loadMore() }
        }
    }
}
