package com.example.netcloudmusic.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException

/**
 * 应用级本地偏好（引导页等）
 *
 * Android Compose 版使用 SharedPreferences；KMP 统一走 DataStore，Android/iOS 共用。
 */
class AppPreferenceRepository(
    private val dataStore: DataStore<Preferences>,
) {
    private companion object {
        val NOT_SHOW_GUIDE_KEY = booleanPreferencesKey("not_show_guide")
    }

    /** 是否已看过引导页（true 表示不再展示） */
    val notShowGuide: Flow<Boolean> = dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences -> preferences[NOT_SHOW_GUIDE_KEY] ?: false }

    suspend fun isNotShowGuide(): Boolean = notShowGuide.first()

    suspend fun setNotShowGuide(notShowGuide: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOT_SHOW_GUIDE_KEY] = notShowGuide
        }
    }
}
