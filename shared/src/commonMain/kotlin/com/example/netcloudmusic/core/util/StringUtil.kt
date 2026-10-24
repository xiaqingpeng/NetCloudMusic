package com.example.netcloudmusic.core.util

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

    /**
     * 验证手机号格式
     * @param phone 手机号
     * @return 是否是有效的手机号
     */
    fun isPhone(phone: String): Boolean {
        if (phone.isBlank()) return false
        // 中国大陆手机号：1开头，第二位是3-9，共11位
        val regex = "^1[3-9]\\d{9}$".toRegex()
        return regex.matches(phone)
    }

    /**
     * 验证验证码格式
     * @param code 验证码
     * @return 是否是有效的验证码（6位数字）
     */
    fun isCode(code: String): Boolean {
        if (code.isBlank()) return false
        // 6位数字验证码
        val regex = "^\\d{6}$".toRegex()
        return regex.matches(code)
    }
}