package com.example.netcloudmusic.feature.discovery

import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.model.ViewData

data class DiscoveryUiState(
    val topDatum: List<ViewData> = listOf(),
    val datum: List<Product> = listOf(),


    /**
     * 0:空闲中
     *
     * 1：下拉刷新中
     * 2：下拉刷新失败
     * 3：下拉刷新成功
     */
    val loadState: Int = 0,

    /**
     * 0：上拉加载空闲中
     * 1：上拉加载中
     * 2：上拉加载失败
     * 3：上拉加载成功
     * 4：没有更多数据
     */
    val loadMoreState: Int = 0,

    val errorMessage: String? = null,
)