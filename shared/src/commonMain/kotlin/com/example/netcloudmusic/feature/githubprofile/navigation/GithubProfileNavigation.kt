package com.example.netcloudmusic.feature.githubprofile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.githubprofile.GithubProfileRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val GITHUB_PROFILE_ROUTE = "github_profile"

fun NavController.navigateToGithubProfile() = navigate(GITHUB_PROFILE_ROUTE)

fun NavGraphBuilder.githubProfileScreen(toBack: () -> Unit) {
    myComposable(GITHUB_PROFILE_ROUTE) {
        GithubProfileRoute(toBack = toBack)
    }
}
