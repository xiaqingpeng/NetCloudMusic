package com.example.netcloudmusic.core.network

/** 由共享 HTTP 客户端读取的登录会话。 */
object NetworkSession {
    var session: String = ""

    fun clear() {
        session = ""
    }
}
