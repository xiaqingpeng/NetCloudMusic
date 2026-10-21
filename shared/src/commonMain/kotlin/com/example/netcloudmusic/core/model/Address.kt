package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

/**
 * 收货地址模型
 */
@Serializable
data class Address(
    val name: String = "",
    val telephone: String = "",
    val province: String = "",
    val provinceCode: String = "",
    val city: String = "",
    val cityCode: String = "",
    val area: String = "",
    val areaCode: String = "",
    val detail: String = "",
    /** 是否是默认地址 */
    val default: Boolean = false,
    val id: String = "",
) {
    /** 省市区拼接，直辖市省市同名时合并显示 */
    fun getReceiverArea(): String = when {
        province == city -> "$province$area"
        else -> "$province$city$area"
    }

    /** 收货人姓名 + 手机号 */
    fun getContact(): String = "$name $telephone"

    val areaDisplay: String
        get() = when {
            province == city -> "$province$area"
            else -> "$province$city$area"
        }
}
