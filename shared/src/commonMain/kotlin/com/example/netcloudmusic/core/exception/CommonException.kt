package com.example.netcloudmusic.core.exception

import com.example.netcloudmusic.core.model.response.NetworkResponse

/** 网络请求失败时的统一异常。 */
class CommonException(
    val networkResponse: NetworkResponse<*>? = null,
    val throwable: Throwable? = null,
) : RuntimeException(throwable)
