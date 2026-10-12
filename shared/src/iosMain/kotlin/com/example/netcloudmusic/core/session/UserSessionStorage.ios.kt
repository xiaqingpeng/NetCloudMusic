package com.example.netcloudmusic.core.session

import platform.Foundation.NSUserDefaults

actual object UserSessionStorage {
    private const val SESSION = "netcloud_music_session"
    private const val USER = "netcloud_music_user"
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun loadSession(): String? = defaults.stringForKey(SESSION)
    actual fun loadUser(): String? = defaults.stringForKey(USER)

    actual fun save(session: String, user: String) {
        defaults.setObject(session, forKey = SESSION)
        defaults.setObject(user, forKey = USER)
    }

    actual fun clear() {
        defaults.removeObjectForKey(SESSION)
        defaults.removeObjectForKey(USER)
    }
}
