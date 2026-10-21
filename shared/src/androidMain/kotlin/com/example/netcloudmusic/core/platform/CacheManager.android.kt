package com.example.netcloudmusic.core.platform

import android.content.Context

actual fun getCacheSize(context: Any?): String {
    val ctx = context as? Context ?: return "0 B"
    return runCatching {
        val cacheDir = ctx.cacheDir
        val size = cacheDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
        formatSize(size)
    }.getOrDefault("0 B")
}

actual fun clearCache(context: Any?) {
    val ctx = context as? Context ?: return
    runCatching {
        ctx.cacheDir.walkTopDown().forEach { it.delete() }
        // 外部缓存目录（如图片缓存）
        ctx.externalCacheDir?.walkTopDown()?.forEach { it.delete() }
    }
}

private fun formatSize(bytes: Long): String = when {
    bytes < 1024L          -> "$bytes B"
    bytes < 1024L * 1024   -> "${"%.1f".format(bytes / 1024.0)} KB"
    bytes < 1024L * 1024 * 1024 -> "${"%.1f".format(bytes / 1024.0 / 1024.0)} MB"
    else                   -> "${"%.1f".format(bytes / 1024.0 / 1024.0 / 1024.0)} GB"
}
