package com.example.netcloudmusic

import androidx.compose.ui.window.ComposeUIViewController
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.ui.MyApp

fun MainViewController() = ComposeUIViewController {
    AppContainerProvider.initialize()
    MyApp()
}
