package com.example.netcloudmusic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.netcloudmusic.core.data.repository.ThemeMode

private val LightColors = lightColorScheme(
    primary = Color(0xFFF04142),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA92E29),
    onPrimaryContainer = Color.White,
    secondary = Color(0x4CFFFFFF),
    onSecondary = Color.Black,
    background = Color(0xFFEDEDED),
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFEDEDED),
    onSurface = Color(0xFF201A1A),
    surfaceVariant = Color.White,
    onSurfaceVariant = Color(0xFF534342),
    outline = Color(0x99000000),
    outlineVariant = Color(0xFFF5F5F5),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFF04142),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF930013),
    onPrimaryContainer = Color(0xFFFFDAD7),
    secondary = Color(0xFFF04142),
    onSecondary = Color(0xFF442927),
    background = Color(0xFF111111),
    onBackground = Color(0xFFEDE0DE),
    surface = Color(0xFF201A1A),
    onSurface = Color(0xFFEDE0DE),
    surfaceVariant = Color(0xFF181818),
    onSurfaceVariant = Color(0xFFD8C2BF),
    outline = Color(0xFFA08C8A),
    outlineVariant = Color(0xFF534342),
)

@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
