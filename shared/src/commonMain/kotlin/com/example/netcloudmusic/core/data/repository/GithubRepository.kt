package com.example.netcloudmusic.core.data.repository

import com.example.netcloudmusic.core.config.Config
import com.example.netcloudmusic.core.model.GithubAccessToken
import com.example.netcloudmusic.core.model.GithubUser
import com.example.netcloudmusic.core.model.GithubRepo
import com.example.netcloudmusic.core.network.NetworkJson
import com.example.netcloudmusic.core.network.platformHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.http.parametersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * GitHub OAuth 仓库。
 *
 * token 换取不能复用业务 HttpClient（会自动带 Authorization），
 * 因此使用独立 plain client。
 */
class GithubRepository(
    private val userRepository: UserRepository,
) {
    private var cachedToken: String = ""
    private var cachedUser: GithubUser? = null

    private val githubHttpClient: HttpClient by lazy {
        platformHttpClient {
            install(ContentNegotiation) {
                json(NetworkJson)
            }
        }
    }

    val isLoggedIn: Boolean
        get() = token.isNotBlank()

    private val token: String
        get() = cachedToken.ifBlank { userRepository.getLocalUser()?.token.orEmpty() }

    /** 用 OAuth code 换 token，拉取用户，并写入本地登录态。 */
    fun loginWithCode(code: String): Flow<GithubUser> = flow {
        val tokenResponse = githubHttpClient.submitForm(
            url = "${Config.GITHUB_SITE_BASE_URL}login/oauth/access_token",
            formParameters = parametersOf(
                "client_id" to listOf(Config.GITHUB_CLIENT_ID),
                "client_secret" to listOf(Config.GITHUB_CLIENT_SECRET),
                "code" to listOf(code),
                "redirect_uri" to listOf(Config.GITHUB_REDIRECT_URI),
            ),
        ) {
            header(HttpHeaders.Accept, "application/json")
        }.body<GithubAccessToken>()

        if (!tokenResponse.error.isNullOrBlank()) {
            throw Exception(tokenResponse.errorDescription ?: tokenResponse.error)
        }
        val token = tokenResponse.accessToken
        if (token.isBlank()) throw Exception("获取 GitHub token 失败")

        cachedToken = token
        val user = githubHttpClient.get("${Config.GITHUB_API_BASE_URL}user") {
            header(HttpHeaders.Authorization, "Bearer $token")
            header(HttpHeaders.Accept, "application/vnd.github+json")
        }.body<GithubUser>()

        cachedUser = user
        userRepository.loginWithGithub(user, token)
        emit(user)
    }

    fun getUser(): Flow<GithubUser> = flow {
        val accessToken = token
        if (accessToken.isBlank()) throw Exception("未登录 GitHub")
        emit(cachedUser ?: githubHttpClient.get("${Config.GITHUB_API_BASE_URL}user") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            header(HttpHeaders.Accept, "application/vnd.github+json")
        }.body<GithubUser>().also { cachedUser = it })
    }

    fun getRepos(page: Int = 1): Flow<List<GithubRepo>> = flow {
        val accessToken = token
        if (accessToken.isBlank()) throw Exception("未登录 GitHub")
        emit(githubHttpClient.get("${Config.GITHUB_API_BASE_URL}user/repos") {
            parameter("page", page)
            parameter("per_page", 30)
            parameter("sort", "updated")
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            header(HttpHeaders.Accept, "application/vnd.github+json")
        }.body<List<GithubRepo>>())
    }

    fun logout() {
        cachedToken = ""
        cachedUser = null
    }
}
