package com.example.netcloudmusic.core.util

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual object SuperDateUtil {
    actual fun currentYear(): Int =
        NSCalendar.currentCalendar.component(NSCalendarUnitYear, fromDate = NSDate()).toInt()

    actual fun yyyyMMddHHmmss(value: String?): String {
        if (value.isNullOrBlank()) return ""
        return try {
            val formatter = NSDateFormatter()
            formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
            formatter.locale = NSLocale.currentLocale
            val date = parse(value)
            formatter.stringFromDate(date)
        } catch (_: Exception) {
            value
        }
    }

    actual fun paymentDeadline(value: String?): String {
        if (value.isNullOrBlank()) return ""
        return try {
            val formatter = NSDateFormatter()
            formatter.dateFormat = "HH:mm:ss"
            formatter.locale = NSLocale.currentLocale
            val date = NSDate(timeIntervalSinceReferenceDate = parse(value).timeIntervalSinceReferenceDate + 15 * 60)
            formatter.stringFromDate(date)
        } catch (_: Exception) {
            ""
        }
    }

    private fun parse(value: String): NSDate {
        value.toLongOrNull()?.let { timestamp ->
            return NSDate(timeIntervalSinceReferenceDate = timestamp / 1000.0 - 978307200.0)
        }
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX",
            "yyyy-MM-dd'T'HH:mm:ssXXXXX",
        )
        patterns.forEach { pattern ->
            val formatter = NSDateFormatter().apply {
                dateFormat = pattern
                locale = NSLocale(localeIdentifier = "en_US_POSIX")
            }
            formatter.dateFromString(value)?.let { return it }
        }
        error("Unsupported date: $value")
    }
}
