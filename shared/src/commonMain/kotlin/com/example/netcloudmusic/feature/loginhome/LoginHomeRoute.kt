package com.example.netcloudmusic.feature.loginhome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.config.Config
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.util.rememberExternalUrlOpener
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.code_login
import netcloudmusic.shared.generated.resources.confirm
import netcloudmusic.shared.generated.resources.github_login
import netcloudmusic.shared.generated.resources.ic_github
import netcloudmusic.shared.generated.resources.login_error
import netcloudmusic.shared.generated.resources.splash_logo
import netcloudmusic.shared.generated.resources.user_agreement
import netcloudmusic.shared.generated.resources.user_agreement_and
import netcloudmusic.shared.generated.resources.user_agreement_privacy_policy
import netcloudmusic.shared.generated.resources.user_agreement_start
import netcloudmusic.shared.generated.resources.username_login
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginHomeRoute(
    toBack: () -> Unit,
    toLogin: () -> Unit = {},
    toCodeLogin: () -> Unit = {},
    finishAllLoginPages: () -> Unit = {},
    onAgreementClick: (Boolean) -> Unit = {},
) {
    val viewModel: LoginHomeViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                LoginHomeViewModel(
                    githubRepository = AppContainerProvider.instance.githubRepository,
                )
            }
        },
    )
    val uiState by viewModel.uiState.collectAsState()
    val tipError by viewModel.tipError.collectAsState()
    val authController = rememberThirdPartyAuthController(viewModel)

    LaunchedEffect(uiState) {
        if (uiState is ThirdPartyLoginUiState.Success) {
            finishAllLoginPages()
        }
    }

    if (tipError != null) {
        AlertDialog(
            onDismissRequest = viewModel::clearError,
            title = { Text(stringResource(Res.string.login_error)) },
            text = { Text(tipError.orEmpty()) },
            confirmButton = {
                TextButton(onClick = viewModel::clearError) {
                    Text(stringResource(Res.string.confirm))
                }
            },
        )
    }

    LoginHomeScreen(
        toBack = toBack,
        toLogin = toLogin,
        toCodeLogin = toCodeLogin,
        onAgreementClick = onAgreementClick,
        isLoading = uiState is ThirdPartyLoginUiState.Loading,
        onGithubLogin = authController::loginWithGithub,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginHomeScreen(
    toBack: () -> Unit = {},
    toLogin: () -> Unit = {},
    toCodeLogin: () -> Unit = {},
    onAgreementClick: (Boolean) -> Unit = {},
    isLoading: Boolean = false,
    onGithubLogin: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(toBack = toBack)
        },
    ) { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues)) {
            Image(
                painter = painterResource(Res.drawable.splash_logo),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 56.dp).size(100.dp),
            )
            Column(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(onClick = toCodeLogin, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(Res.string.code_login))
                }
                OutlinedButton(onClick = toLogin, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(Res.string.username_login))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = "或",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                OutlinedButton(
                    onClick = onGithubLogin,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                         Image(
                             painter = painterResource(Res.drawable.ic_github),
                             contentDescription = null,
                             modifier = Modifier.size(18.dp),
                         )
                    }
                    Text(
                        text = stringResource(Res.string.github_login),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }

                UserAgreement(onAgreementClick, Modifier.padding(top = 16.dp))
            }
        }
    }
}

@Composable
private fun UserAgreement(
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val openUrl = rememberExternalUrlOpener()
    val agreement = stringResource(Res.string.user_agreement)
    val agreementStart = stringResource(Res.string.user_agreement_start)
    val text = buildAnnotatedString {
        append(agreementStart)
        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(agreement)
        }
        append(stringResource(Res.string.user_agreement_and))
        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(stringResource(Res.string.user_agreement_privacy_policy))
        }
    }
    ClickableText(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = modifier,
        onClick = { offset ->
            val url = if (offset < agreementStart.length + agreement.length) {
                Config.LINK_USER_USER_AGREEMENT
            } else {
                Config.LINK_USER_PRIVACY_POLICY
            }
            onClick(offset < agreementStart.length + agreement.length)
            openUrl(url)
        },
    )
}
