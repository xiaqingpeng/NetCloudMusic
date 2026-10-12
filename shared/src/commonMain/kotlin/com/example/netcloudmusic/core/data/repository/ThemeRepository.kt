package com.example.netcloudmusic.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}

class ThemeRepository(
    private val dataStore: DataStore<Preferences>,
) {
    private companion object {
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }

    val themeMode: Flow<ThemeMode> = dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences ->
            preferences[THEME_MODE_KEY]
                ?.let { value -> runCatching { ThemeMode.valueOf(value) }.getOrNull() }
                ?: ThemeMode.SYSTEM
        }

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }

    suspend fun toggleTheme() {
        val currentMode = themeMode.first()
        setThemeMode(if (currentMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK)
    }
}
