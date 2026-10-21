package com.example.netcloudmusic.core.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun getCacheSize(context: Any?): String {
    return runCatching {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory, NSUserDomainMask, true,
        )
        val cachePath = paths.firstOrNull() as? String ?: return "0 B"
        val size = NSFileManager.defaultManager.let { fm ->
            fm.subpathsOfDirectoryAtPath(cachePath, null)
                ?.filterIsInstance<String>()
                ?.sumOf { name ->
                    val full = "$cachePath/$name"
                    val attrs = fm.attributesOfItemAtPath(full, null)
                    (attrs?.get("NSFileSize") as? Long) ?: 0L
                } ?: 0L
        }
        formatSize(size)
    }.getOrDefault("0 B")
}

@OptIn(ExperimentalForeignApi::class)
actual fun clearCache(context: Any?) {
    runCatching {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory, NSUserDomainMask, true,
        )
        val cachePath = paths.firstOrNull() as? String ?: return
        val fm = NSFileManager.defaultManager
        fm.subpathsOfDirectoryAtPath(cachePath, null)
            ?.filterIsInstance<String>()
            ?.forEach { name -> fm.removeItemAtPath("$cachePath/$name", null) }
    }
}

private fun formatSize(bytes: Long): String = when {
    bytes < 1024L               -> "$bytes B"
    bytes < 1024L * 1024        -> "${(bytes * 10 / 1024) / 10.0} KB"
    bytes < 1024L * 1024 * 1024 -> "${(bytes * 10 / 1024 / 1024) / 10.0} MB"
    else                        -> "${(bytes * 10 / 1024 / 1024 / 1024) / 10.0} GB"
}
