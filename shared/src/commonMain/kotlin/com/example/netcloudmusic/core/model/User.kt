package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 用户模型。
 *
 * 当前 API 主要使用 phone/password 登录，其余字段用于兼容服务端返回的用户信息。
 */
@Serializable
data class User(
    val id: String? = null,
    val nickname: String? = null,
    val icon: String? = null,
    val detail: String? = null,
    val gender: String = "0",
    val birthday: String? = null,
    val bg: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val password: String? = null,
    val token: String? = null,
    val refreshToken: String? = null,
    val openid: String? = null,
    val expiresIn: Long? = null,
    val style: Int? = null,
    val followingsCount: Long = 0,
    val followersCount: Long = 0,
    val commentsCount: Long = 0,
    /** 验证码（验证码登录时传递） */
    val code: String? = null,
    val province: String? = null,
    val provinceCode: String? = null,
    val city: String? = null,
    val cityCode: String? = null,
    val area: String? = null,
    val areaCode: String? = null,
) {
    /** 格式化性别：0=保密 10=男 20=女 */
    fun getGenderFormat(): String = when (gender) {
        "10" -> "男"
        "20" -> "女"
        else -> "保密"
    }
}
