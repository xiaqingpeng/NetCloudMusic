package com.example.netcloudmusic.feature.discovery.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.netcloudmusic.core.design.component.MyErrorView
import com.example.netcloudmusic.core.design.component.MyLoadMoreLoading
import com.example.netcloudmusic.core.design.component.MyLoading
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.extension.isScrollingToEnd
import com.example.netcloudmusic.core.util.Constant
import com.example.netcloudmusic.feature.discovery.DiscoveryUiState
import com.example.netcloudmusic.feature.discovery.LoadMoreState
import com.example.netcloudmusic.feature.product.component.ItemProductGrid
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.empty_data
import netcloudmusic.shared.generated.resources.pull_up_load_more
import netcloudmusic.shared.generated.resources.recommand_for_you
import org.jetbrains.compose.resources.stringResource

/**
 * 发现界面内容区
 *
 * 包含下拉刷新、上拉加载更多、顶部区块（Banner/按钮/大图/热门）、商品列表及叠加层视图。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryContentView(
    uiState: DiscoveryUiState,
    toUri: (String) -> Unit,
    loadData: () -> Unit,
    loadMore: () -> Unit,
    toProductDetail: (String) -> Unit,
    clearError: () -> Unit,
) {
    val gridState = rememberLazyGridState()
    val isLoadMoreTriggered by remember {
        derivedStateOf { gridState.isScrollingToEnd() }
    }

    // 在 Composable 上下文里提前读好字符串
    val labelIdle = stringResource(Res.string.pull_up_load_more)

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isInitialLoading -> {
                MyLoading(Modifier.fillMaxSize())
            }
            
            uiState.topDatum.isEmpty() && uiState.datum.isEmpty() -> {
                MyErrorView(
                    message = uiState.errorMessage ?: stringResource(Res.string.empty_data),
                    onRetryClick = loadData,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            else -> {
                PullToRefreshBox(
                    state = rememberPullToRefreshState(),
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = loadData,
                ) {
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(bottom = SpaceExtraMedium),
                        verticalArrangement = Arrangement.spacedBy(SpaceExtraMedium),
                        horizontalArrangement = Arrangement.spacedBy(SpaceExtraMedium),
                        modifier = Modifier.padding(horizontal = SpaceExtraMedium),
                    ) {
                        topSectionItems(uiState, toUri, toProductDetail)
                        productItems(uiState, toProductDetail)
                        loadMoreItem(
                            uiState = uiState,
                            loadMore = loadMore,
                            labelIdle = labelIdle,
                        )
                    }

                    if (isLoadMoreTriggered) {
                        LaunchedEffect(Unit) { loadMore() }
                    }
                }
            }
        }
    }
}

// ─── 顶部区块：Banner / 按钮 / 大图 / 热门商品 ─────────────────────────────────
private fun LazyGridScope.topSectionItems(
    uiState: DiscoveryUiState,
    toUri: (String) -> Unit,
    toProductDetail: (String) -> Unit,
) {
    items(
        uiState.topDatum,
        span = { GridItemSpan(maxLineSpan) },
    ) { data ->
        when (data.style) {
            Constant.VALUE10 -> DiscoveryBanner(
                data     = data.ads.orEmpty(),
                onAdClick = { ad -> ad.uri?.let { toUri(it) } },
            )
            Constant.VALUE20 -> DiscoveryButton(data = data.buttons.orEmpty())
            Constant.VALUE30 -> CropButtonColumn(data = data.larges.orEmpty(), toUri = toUri)
            Constant.VALUE40 -> HotProductColumn(data = data.hots.orEmpty(), toProductDetail = toProductDetail)
        }
    }

    if (uiState.topDatum.isNotEmpty()) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            ProductItemTitle(stringResource(Res.string.recommand_for_you))
        }
    }
}

// ─── 商品列表 ────────────────────────────────────────────────────────────────
private fun LazyGridScope.productItems(
    uiState: DiscoveryUiState,
    toProductDetail: (String) -> Unit,
) {
    items(uiState.datum) { product ->
        ItemProductGrid(
            data     = product,
            modifier = Modifier.clickable { toProductDetail(product.id) },
        )
    }
}

// ─── 上拉加载状态 footer ──────────────────────────────────────────────────────
private fun LazyGridScope.loadMoreItem(
    uiState: DiscoveryUiState,
    loadMore: () -> Unit,
    labelIdle: String,
) {
    if (uiState.datum.isEmpty()) return

    item(span = { GridItemSpan(maxLineSpan) }) {
        Box(
            modifier = Modifier.fillMaxSize().padding(SpaceExtraMedium),
            contentAlignment = Alignment.Center
        ) {
            when (uiState.loadMoreState) {
                LoadMoreState.Idle -> Text(labelIdle)
                LoadMoreState.Loading -> MyLoadMoreLoading()
                LoadMoreState.Error -> Text("加载失败，点击重试", modifier = Modifier.clickable { loadMore() })
                LoadMoreState.Done -> Text("加载成功")
                LoadMoreState.NoMore -> Text("没有更多了")
            }
        }
    }
}
