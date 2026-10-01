package com.example.netcloudmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.netcloudmusic.core.config.Config
import com.example.netcloudmusic.ui.MyApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        Config.configure(
            endpoint = BuildConfig.ENDPOINT,
            resourceEndpoint = BuildConfig.RESOURCE_ENDPOINT
        )

        setContent {
            MyApp()
        }
    }
}
