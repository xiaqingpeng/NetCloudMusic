package com.example.netcloudmusic.core.util

import com.example.netcloudmusic.core.config.Config

object ResourceUtil {
    /**
     * 将相对资源转为绝对路径
     *
     * @param uri 放到自己服务端的资源以files开头，其他资源都在阿里云oss
     * @return
     */
    fun r(uri: String): String {
        val value = uri.trim()
        if (value.isEmpty()) return value

        // 接口中既可能返回完整 URL，也可能返回相对资源路径。
        // Coil 在 iOS 上不会把 “files/...” 当成 URL，必须在这里补上服务端地址。
        if (value.startsWith("http://") || value.startsWith("https://")) {
            return value
        }

        if (value.startsWith("//")) {
            return "https:$value"
        }

        val path = value.trimStart('/')
        return if (path.startsWith("files/") || path == "files") {
            Config.ENDPOINT.trimEnd('/') + "/" + path
        } else {
            Config.RESOURCE_ENDPOINT
                .replace("%s", path)
        }
    }
}
