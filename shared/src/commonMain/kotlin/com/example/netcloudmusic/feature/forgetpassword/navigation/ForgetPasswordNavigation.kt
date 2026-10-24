package com.example.netcloudmusic.feature.forgetpassword.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.feature.forgetpassword.ForgetPasswordRoute
import com.example.netcloudmusic.feature.forgetpassword.ForgetPasswordViewModel
import com.example.netcloudmusic.ui.navigation.myComposable

const val FORGET_PASSWORD_ROUTE = "forget_password"

fun NavController.navigateToForgetPassword() = navigate(FORGET_PASSWORD_ROUTE)

fun NavGraphBuilder.forgetPasswordScreen(
    toBack: () -> Unit,
    onSuccess: () -> Unit,
) {
    myComposable(route = FORGET_PASSWORD_ROUTE) {
        val viewModel = ForgetPasswordViewModel(
            userRepository = AppContainerProvider.instance.userRepository,
        )
        ForgetPasswordRoute(
            toBack = toBack,
            onSuccess = onSuccess,
            viewModel = viewModel,
        )
    }
}
