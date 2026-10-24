package com.example.netcloudmusic.core.util

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate

actual object SuperDateUtil {
    actual fun currentYear(): Int {
        return NSCalendar.currentCalendar.component(
            NSCalendarUnitYear,
            fromDate = NSDate()
        ).toInt()
    }
}
