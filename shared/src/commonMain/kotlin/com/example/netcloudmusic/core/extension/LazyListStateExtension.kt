package com.example.netcloudmusic.core.extension

import androidx.compose.foundation.lazy.LazyListState

/**
 * 检查 LazyColumn/LazyRow 是否已滚动到底部（最后一个 item 可见）
 */
fun LazyListState.isScrollingToEnd(): Boolean {
    val totalItems = layoutInfo.totalItemsCount
    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    return totalItems > 0 && lastVisibleItemIndex >= totalItems - 1
}
