package com.example.netcloudmusic.feature.loginhome

/**
 * Android 宿主与 shared LoginHome 之间的 GitHub 登录桥。
 *
 * androidApp（MyApplication / Activity）负责启动浏览器 OAuth；
 * shared androidMain 负责把回调写回 ViewModel。
 */
object ThirdPartyAuthBridge {
    var startGithub: (() -> Unit)? = null

    var onGithubCode: ((String) -> Unit)? = null
    var onGithubError: ((String) -> Unit)? = null

    const val ACTION_GITHUB_OAUTH_CALLBACK = "com.example.netcloudmusic.GITHUB_OAUTH_CALLBACK"
    const val EXTRA_CODE = "code"
    const val EXTRA_ERROR = "error"
}
