package com.example.netcloudmusic.feature.discovery

import androidx.compose.runtime.Immutable
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.model.ViewData

// @Immutable 告知 Compose 编译器此类所有字段不会在外部被修改，
// 允许编译器将持有它的 Composable 标记为可跳过（skippable），避免不必要的重组。
@Immutable
data class DiscoveryUiState(
    val topDatum: List<ViewData> = emptyList(),
    val datum: List<Product> = emptyList(),

    /** 首次进入页面的全屏加载状态，与下拉刷新互斥 */
    val isInitialLoading: Boolean = true,

    /** 下拉刷新状态，驱动 PullToRefreshBox 的指示器 */
    val isRefreshing: Boolean = false,

    /** 刷新结果，成功/失败后短暂显示 toast，随后重置为 Idle */
    val refreshResult: RefreshResult = RefreshResult.Idle,

    /** 上拉加载更多状态 */
    val loadMoreState: LoadMoreState = LoadMoreState.Idle,

    val errorMessage: String? = null,
)

/** 下拉刷新的结果状态 */
enum class RefreshResult { Idle, Success, Error }

/** 上拉加载更多的状态 */
enum class LoadMoreState {
    Idle,    // 空闲，显示"上拉加载更多"提示
    Loading, // 加载中
    Error,   // 加载失败，显示重试
    Done,    // 本批加载成功，短暂显示"加载成功"
    NoMore,  // 没有更多数据
}