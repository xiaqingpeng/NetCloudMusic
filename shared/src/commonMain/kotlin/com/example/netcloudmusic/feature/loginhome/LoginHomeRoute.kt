package com.example.netcloudmusic.feature.loginhome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.unit.dp
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.code_login
import netcloudmusic.shared.generated.resources.login
import netcloudmusic.shared.generated.resources.splash_logo
import netcloudmusic.shared.generated.resources.user_agreement
import netcloudmusic.shared.generated.resources.user_agreement_and
import netcloudmusic.shared.generated.resources.user_agreement_privacy_policy
import netcloudmusic.shared.generated.resources.user_agreement_start
import netcloudmusic.shared.generated.resources.username_login
import com.example.netcloudmusic.core.config.Config
import com.example.netcloudmusic.util.rememberExternalUrlOpener
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginHomeRoute(
    toBack: () -> Unit,
    toLogin: () -> Unit = {},
    toCodeLogin: () -> Unit = {},
    onAgreementClick: (Boolean) -> Unit = {},
) {
    LoginHomeScreen(toBack, toLogin, toCodeLogin, onAgreementClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginHomeScreen(
    toBack: () -> Unit = {},
    toLogin: () -> Unit = {},
    toCodeLogin: () -> Unit = {},
    onAgreementClick: (Boolean) -> Unit = {},
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
