package com.example.netcloudmusic.feature.me

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.model.User
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.login_or_register
import netcloudmusic.shared.generated.resources.logout
import netcloudmusic.shared.generated.resources.me_fans
import netcloudmusic.shared.generated.resources.me_friend
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource
import netcloudmusic.shared.generated.resources.splash_logo
import netcloudmusic.shared.generated.resources.me_login_tip
import netcloudmusic.shared.generated.resources.unknown_user

/**
 * 我的界面
 */
@Composable
fun MeRoute(toLogin: () -> Unit = {}) {
    val viewModel: MeViewModel = viewModel(
        factory = viewModelFactory {
            initializer { MeViewModel(AppContainerProvider.instance.userRepository) }
        },
    )
    val state by viewModel.uiState.collectAsState()
    MeScreen(state, toLogin, viewModel::logout)
}

@Composable
fun MeScreen(
    state: MeUiState = MeUiState(),
    toLogin: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.user != null) {
            UserProfileSection(state.user, onLogout)
        } else {
            LoginPromptSection(toLogin)
        }
    }
}

@Composable
private fun UserProfileSection(user: User, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            model = user.icon,
            contentDescription = null,
            modifier = Modifier.size(88.dp).clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(Res.drawable.splash_logo),
            error = painterResource(Res.drawable.splash_logo),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            user.nickname ?: user.id ?: stringResource(Res.string.unknown_user),
            style = MaterialTheme.typography.titleLarge,
        )
        user.detail?.takeIf(String::isNotBlank)?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            MeStatItem(stringResource(Res.string.me_friend), user.followingsCount)
            MeStatItem(stringResource(Res.string.me_fans), user.followersCount)
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(Res.string.logout),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.clickable(onClick = onLogout),
        )
    }
}

@Composable
private fun LoginPromptSection(toLogin: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.splash_logo),
            contentDescription = null,
            modifier = Modifier.size(88.dp).clip(CircleShape),
        )
        Text(
            text = stringResource(Res.string.login_or_register),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp).clickable(onClick = toLogin),
        )
        Text(
            text = stringResource(Res.string.me_login_tip),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun MeStatItem(label: String, count: Long) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count.toString(), style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
