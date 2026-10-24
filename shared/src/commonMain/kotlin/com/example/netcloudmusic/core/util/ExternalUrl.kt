package com.example.netcloudmusic.core.util

import androidx.compose.runtime.Composable

/** Remembers an action that opens a URL in the platform's default browser. */
@Composable
expect fun rememberExternalUrlOpener(): (String) -> Unit
