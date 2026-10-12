package com.example.netcloudmusic.core.session

import android.content.Context

actual object UserSessionStorage {
    private const val PREF_NAME = "netcloud_music_user"
    private const val SESSION = "session"
    private const val USER = "user"
    private var preferences: android.content.SharedPreferences? = null

    fun initialize(context: Context) {
        preferences = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    actual fun loadSession(): String? = preferences?.getString(SESSION, null)
    actual fun loadUser(): String? = preferences?.getString(USER, null)

    actual fun save(session: String, user: String) {
        preferences?.edit()?.putString(SESSION, session)?.putString(USER, user)?.apply()
    }

    actual fun clear() {
        preferences?.edit()?.clear()?.apply()
    }
}
