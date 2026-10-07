package com.example.netcloudmusic.util

actual object NetworkUtil {
    actual fun initialize(context: Any) = Unit

    // iOS 网络状态由宿主应用管理；未接入 NWPathMonitor 前默认允许请求继续执行。
    actual fun isNetworkAvailable(): Boolean = true
}
