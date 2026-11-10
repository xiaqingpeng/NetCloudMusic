package com.example.netcloudmusic.github

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.netcloudmusic.feature.loginhome.ThirdPartyAuthBridge

/**
 * 接收 GitHub OAuth 深链回调的透明 Activity。
 * `netcloudmusic://github-callback?code=...`
 */
class GithubCallbackActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent?.data
        val code = uri?.getQueryParameter("code")
        val error = uri?.getQueryParameter("error")

        when {
            code != null -> {
                sendBroadcast(
                    Intent(ThirdPartyAuthBridge.ACTION_GITHUB_OAUTH_CALLBACK).apply {
                        putExtra(ThirdPartyAuthBridge.EXTRA_CODE, code)
                        setPackage(packageName)
                    }
                )
            }
            error != null -> {
                sendBroadcast(
                    Intent(ThirdPartyAuthBridge.ACTION_GITHUB_OAUTH_CALLBACK).apply {
                        putExtra(ThirdPartyAuthBridge.EXTRA_ERROR, error)
                        setPackage(packageName)
                    }
                )
            }
        }
        finish()
    }
}
