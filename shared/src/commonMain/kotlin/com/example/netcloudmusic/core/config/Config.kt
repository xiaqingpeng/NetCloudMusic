package com.example.netcloudmusic.core.config

/**
 * 应用接口与资源地址。
 */
object Config {
    const val LINK_USER_USER_AGREEMENT = "https://www.ixuea.com/articles/4449"
    const val LINK_USER_PRIVACY_POLICY = "https://www.ixuea.com/articles/4467"

    var ENDPOINT: String = "https://quick-server-sp.ixuea.com/"
        private set

    var RESOURCE_ENDPOINT: String = "https://rs.ixuea.com/quick/%s"
        private set

    // GitHub OAuth（client_id / secret 由 Android BuildConfig / configure 注入）
    var GITHUB_CLIENT_ID: String = ""
        private set
    var GITHUB_CLIENT_SECRET: String = ""
        private set
    const val GITHUB_REDIRECT_URI = "netcloudmusic://github-callback"
    const val GITHUB_OAUTH_URL = "https://github.com/login/oauth/authorize"
    const val GITHUB_API_BASE_URL = "https://api.github.com/"
    const val GITHUB_SITE_BASE_URL = "https://github.com/"

    /**
     * Android initializes these from its selected build flavor.
     * iOS keeps the production defaults unless configured by its host app.
     */
    fun configure(
        endpoint: String,
        resourceEndpoint: String,
        githubClientId: String = "",
        githubClientSecret: String = "",
    ) {
        ENDPOINT = endpoint
        RESOURCE_ENDPOINT = resourceEndpoint
        GITHUB_CLIENT_ID = githubClientId
        GITHUB_CLIENT_SECRET = githubClientSecret
    }
}
