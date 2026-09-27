package com.example.netcloudmusic.util

/**
 * 日期时间工具类。
 *
 * 各平台使用自己的日期 API 实现，调用方可以继续在 commonMain 中使用。
 */
expect object SuperDateUtil {
    fun currentYear(): Int
}
