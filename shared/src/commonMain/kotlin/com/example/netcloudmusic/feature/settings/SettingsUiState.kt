package com.example.netcloudmusic.feature.settings

import com.example.netcloudmusic.core.data.repository.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isLoggedIn: Boolean = false,
    val loggedOut: Boolean = false,
)
