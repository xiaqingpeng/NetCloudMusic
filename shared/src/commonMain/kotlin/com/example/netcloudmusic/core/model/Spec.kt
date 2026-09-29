package com.example.netcloudmusic.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Spec(
    val id: String,
    val title: String,

    //选择子item信息
    val selectedSubId: String? = null,
    val selectedSubTitle: String? = null,

    val data: List<Spec>? = null,
)