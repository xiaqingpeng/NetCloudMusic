package com.example.netcloudmusic.core.util

import kotlin.math.abs
import kotlin.math.floor

/**
 * 跨平台金额格式化。Kotlin/Native 不支持 JVM 的 String.format。
 */
fun formatPrice(value: Double): String {
    if (!value.isFinite()) return "0.00"
    val scaled = floor(abs(value) * 100.0 + 0.5).toLong()
    return buildString {
        if (value < 0) append('-')
        append(scaled / 100)
        append('.')
        append((scaled % 100).toString().padStart(2, '0'))
    }
}
