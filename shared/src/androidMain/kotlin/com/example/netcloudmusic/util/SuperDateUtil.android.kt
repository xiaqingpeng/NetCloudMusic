package com.example.netcloudmusic.util

import java.util.Calendar

actual object SuperDateUtil {
    actual fun currentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }
}
