package com.example.netcloudmusic.core.exception

import com.example.netcloudmusic.core.util.NetworkUtil
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.bg_error
import org.jetbrains.compose.resources.DrawableResource

/** 将网络异常转换成可以直接展示给用户的文案。 */
fun Throwable.tipMessage(unknownMessage: String = "未知错误，请检查网络以及服务端"): String {
    return if (this is CommonException) {
        networkResponse?.message?.takeIf(String::isNotBlank)
            ?: throwable?.message?.takeIf(String::isNotBlank)
            ?: unknownMessage
    } else if (NetworkUtil.isNetworkAvailable()) {
        message?.takeIf(String::isNotBlank) ?: unknownMessage
    } else {
        "网络未连接，请检查网络设置"
    }
}

fun Throwable.tipIcon(): DrawableResource = Res.drawable.bg_error
