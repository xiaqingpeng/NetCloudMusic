package com.example.netcloudmusic.core.platform

/**
 * 平台缓存管理。
 * @param context Android 传 Context，iOS 传 null
 */
expect fun getCacheSize(context: Any?): String

expect fun clearCache(context: Any?)
