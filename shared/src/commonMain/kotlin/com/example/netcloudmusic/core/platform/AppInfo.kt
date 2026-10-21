package com.example.netcloudmusic.core.platform

/**
 * 平台应用版本信息。
 *
 * Android 通过 PackageManager 获取，iOS 通过 NSBundle 获取。
 * @param context Android 端为 Context，iOS 端忽略
 */
expect fun appVersionName(context: Any?): String

expect fun appVersionCode(context: Any?): String
