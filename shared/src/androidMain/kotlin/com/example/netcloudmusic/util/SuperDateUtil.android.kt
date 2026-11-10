package com.example.netcloudmusic.core.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

actual object SuperDateUtil {
    actual fun currentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

    actual fun yyyyMMddHHmmss(value: String?): String {
        if (value.isNullOrBlank()) return ""
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            sdf.format(parse(value))
        } catch (_: Exception) {
            value
        }
    }

    actual fun paymentDeadline(value: String?): String {
        if (value.isNullOrBlank()) return ""
        return try {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            sdf.format(java.util.Date(parse(value).time + 15 * 60 * 1000))
        } catch (_: Exception) {
            ""
        }
    }

    private fun parse(value: String): java.util.Date {
        value.toLongOrNull()?.let { timestamp ->
            return java.util.Date(timestamp)
        }
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSX",
            "yyyy-MM-dd'T'HH:mm:ssX",
        )
        patterns.forEach { pattern ->
            runCatching {
                return SimpleDateFormat(pattern, Locale.US).apply {
                    isLenient = false
                }.parse(value) ?: error("Invalid date")
            }
        }
        error("Unsupported date: $value")
    }
}
