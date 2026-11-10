package com.example.netcloudmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.netcloudmusic.core.config.Config
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.session.UserSessionStorage
import com.example.netcloudmusic.core.util.NetworkUtil
import com.example.netcloudmusic.ui.MyApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        NetworkUtil.initialize(this)
        UserSessionStorage.initialize(this)
        AppContainerProvider.initialize(this)

        Config.configure(
            endpoint = BuildConfig.ENDPOINT,
            resourceEndpoint = BuildConfig.RESOURCE_ENDPOINT,
            githubClientId = BuildConfig.GITHUB_CLIENT_ID,
            githubClientSecret = BuildConfig.GITHUB_CLIENT_SECRET,
        )
        // Config 注入后再注册一次启动器，避免 Application 阶段读到空的 GitHub Client ID
        MyApplication.instance.registerLaunchers()

        setContent {
            MyApp()
        }
    }
}
