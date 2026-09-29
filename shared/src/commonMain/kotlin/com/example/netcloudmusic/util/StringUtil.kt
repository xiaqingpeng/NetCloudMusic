package com.example.netcloudmusic.util

/**
 * 字符串工具类
 */
object StringUtil {
    fun formatCount(data: Long): String {
        if (data >= 9999) {
            return "${(data * 1.0 / 10000)}万"
        }
        return data.toString()
    }

    /**
     * 是否符合密码格式
     *
     * @param value
     * @return
     */
    fun isPassword(value: String): Boolean {
        return value.length in 6..20
    }

    fun isNickname(data: String): Boolean {
        return data.length in 2..10
    }
}