package com.example.netcloudmusic.core.platform

import android.content.Context

actual fun appVersionName(context: Any?): String {
    val ctx = context as? Context ?: return ""
    return runCatching {
        val info = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
        info.versionName ?: ""
    }.getOrDefault("")
}

actual fun appVersionCode(context: Any?): String {
    val ctx = context as? Context ?: return ""
    return runCatching {
        val info = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
        info.longVersionCode.toString()
    }.getOrDefault("")
}
