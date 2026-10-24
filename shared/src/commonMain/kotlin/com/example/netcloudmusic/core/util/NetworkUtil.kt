package com.example.netcloudmusic.core.util

/** 跨平台网络状态查询。Android 会使用 ConnectivityManager，iOS 由平台实现。 */
expect object NetworkUtil {
    fun initialize(context: Any)
    fun isNetworkAvailable(): Boolean
}
