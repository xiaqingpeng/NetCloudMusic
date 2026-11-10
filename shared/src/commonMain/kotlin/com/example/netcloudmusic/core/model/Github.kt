package com.example.netcloudmusic.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** GitHub OAuth token 换取响应 */
@Serializable
data class GithubAccessToken(
    @SerialName("access_token") val accessToken: String = "",
    @SerialName("token_type") val tokenType: String = "",
    val scope: String = "",
    val error: String? = null,
    @SerialName("error_description") val errorDescription: String? = null,
)

/** GitHub 用户信息（api.github.com/user） */
@Serializable
data class GithubUser(
    val login: String,
    val id: Long = 0,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    val name: String? = null,
    val bio: String? = null,
    val company: String? = null,
    val location: String? = null,
    val blog: String? = null,
    val email: String? = null,
    @SerialName("html_url") val htmlUrl: String? = null,
    val followers: Int = 0,
    val following: Int = 0,
    @SerialName("public_repos") val publicRepos: Int = 0,
    @SerialName("public_gists") val publicGists: Int = 0,
    @SerialName("created_at") val createdAt: String? = null,
)

@Serializable
data class GithubRepo(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val language: String? = null,
    val fork: Boolean = false,
    @SerialName("html_url") val htmlUrl: String? = null,
    @SerialName("stargazers_count") val stars: Int = 0,
)
