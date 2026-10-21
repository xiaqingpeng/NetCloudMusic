package com.example.netcloudmusic.feature.me.model

import org.jetbrains.compose.resources.DrawableResource

/**
 * "更多服务"按钮数据模型
 */
data class ButtonControl(
    val id: Int,
    /** 图标网络相对路径，通过 ResourceUtil.r() 拼接成完整 URL */
    val icon: String,
    val title: String,
    val key: String,
)

/**
 * 订单状态按钮数据模型
 */
data class OrderButtonItem(
    /** CMP 共享 drawable 资源 */
    val icon: DrawableResource,
    val title: String,
)
