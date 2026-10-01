package com.example.netcloudmusic.util

import com.example.netcloudmusic.core.config.Config

object ResourceUtil {
    /**
     * 将相对资源转为绝对路径
     *
     * @param uri 放到自己服务端的资源以files开头，其他资源都在阿里云oss
     * @return
     */
    fun r(uri: String): String {
        if (uri.startsWith("http")) {
            return uri
        }
        return if (uri.startsWith("files")) {
            uri
        } else {
            Config.RESOURCE_ENDPOINT.replace("%s", uri)
        }
    }
}
