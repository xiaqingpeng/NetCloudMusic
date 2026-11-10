package com.example.netcloudmusic

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.example.netcloudmusic.core.config.Config
import com.example.netcloudmusic.feature.loginhome.ThirdPartyAuthBridge
import java.net.URLEncoder

/**
 * Android 宿主 Application：向 shared 层注册 GitHub 登录启动器。
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerLaunchers()
    }

    /**
     * 注册 GitHub 登录启动器。
     * MainActivity 在 Config.configure 之后会再调一次，确保点击时配置已就绪。
     */
    fun registerLaunchers() {
        ThirdPartyAuthBridge.startGithub = startGithub@{
            if (Config.GITHUB_CLIENT_ID.isBlank() || Config.GITHUB_CLIENT_SECRET.isBlank()) {
                ThirdPartyAuthBridge.onGithubError?.invoke("未配置 GitHub Client ID")
                return@startGithub
            }
            val scope = "read:user"
            val url = buildString {
                append(Config.GITHUB_OAUTH_URL)
                append("?client_id=").append(URLEncoder.encode(Config.GITHUB_CLIENT_ID, "UTF-8"))
                append("&redirect_uri=").append(URLEncoder.encode(Config.GITHUB_REDIRECT_URI, "UTF-8"))
                append("&scope=").append(URLEncoder.encode(scope, "UTF-8"))
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }
}
