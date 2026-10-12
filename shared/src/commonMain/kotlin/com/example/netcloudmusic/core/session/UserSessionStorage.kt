package com.example.netcloudmusic.core.session

/** 跨平台保存登录会话和用户信息。 */
expect object UserSessionStorage {
    fun loadSession(): String?
    fun loadUser(): String?
    fun save(session: String, user: String)
    fun clear()
}
