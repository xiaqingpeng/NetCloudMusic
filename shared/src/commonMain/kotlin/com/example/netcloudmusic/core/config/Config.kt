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

    /**
     * Android initializes these from its selected build flavor.
     * iOS keeps the production defaults unless configured by its host app.
     */
    fun configure(endpoint: String, resourceEndpoint: String) {
        ENDPOINT = endpoint
        RESOURCE_ENDPOINT = resourceEndpoint
    }
}
