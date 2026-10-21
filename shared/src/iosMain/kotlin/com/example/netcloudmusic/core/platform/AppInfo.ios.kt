package com.example.netcloudmusic.core.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle

@OptIn(ExperimentalForeignApi::class)
actual fun appVersionName(context: Any?): String {
    return NSBundle.mainBundle.infoDictionary
        ?.get("CFBundleShortVersionString") as? String ?: ""
}

@OptIn(ExperimentalForeignApi::class)
actual fun appVersionCode(context: Any?): String {
    return NSBundle.mainBundle.infoDictionary
        ?.get("CFBundleVersion") as? String ?: ""
}
