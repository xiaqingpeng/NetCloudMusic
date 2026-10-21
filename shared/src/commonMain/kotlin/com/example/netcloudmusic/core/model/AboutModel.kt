package com.example.netcloudmusic.core.model

/**
 * 关于页面数据模型
 */
data class AboutModel(
    val versionCode: Long = 0,
    val versionName: String = "",
    val isDownloading: Boolean = false,
    val downloadStarted: Boolean = false,
    val downloadProgress: Int = 0,
    val cacheSize: String = "计算中...",
    val isClearingCache: Boolean = false,
)
