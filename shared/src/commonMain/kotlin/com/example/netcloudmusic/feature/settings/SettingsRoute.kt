package com.example.netcloudmusic.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.data.repository.ThemeMode
import com.example.netcloudmusic.core.di.AppContainerProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.cancel
import netcloudmusic.shared.generated.resources.confirm
import netcloudmusic.shared.generated.resources.logout
import netcloudmusic.shared.generated.resources.theme_dark
import netcloudmusic.shared.generated.resources.theme_light
import netcloudmusic.shared.generated.resources.theme_mode
import netcloudmusic.shared.generated.resources.theme_system
import netcloudmusic.shared.generated.resources.settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsRoute(
    toBack: () -> Unit,
    toLogin: () -> Unit = {},
) {
    val viewModel: SettingsViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    themeRepository = AppContainerProvider.instance.themeRepository,
                    userRepository = AppContainerProvider.instance.userRepository,
                    githubRepository = AppContainerProvider.instance.githubRepository,
                )
            }
        },
    )
    val state by viewModel.uiState.collectAsState()

    // 退出登录后跳回登录页
    LaunchedEffect(state.loggedOut) {
        if (state.loggedOut) {
            viewModel.consumeLoggedOut()
            toLogin()
        }
    }

    SettingsScreen(
        themeMode = state.themeMode,
        isLoggedIn = state.isLoggedIn,
        toBack = toBack,
        onThemeModeSelected = viewModel::setThemeMode,
        onLogout = viewModel::logout,
    )
}

@Composable
private fun SettingsScreen(
    themeMode: ThemeMode,
    isLoggedIn: Boolean,
    toBack: () -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onLogout: () -> Unit,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(stringResource(Res.string.logout)) },
            text = { Text("确认退出登录？") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) { Text(stringResource(Res.string.confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(stringResource(Res.string.cancel))
                }
            },
        )
    }

    Scaffold(
        topBar = {
            MyCenterTopAppBar(title = stringResource(Res.string.settings), toBack = toBack)
        },
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            // 主题设置
            Text(
                text = stringResource(Res.string.theme_mode),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            )
            ThemeModeOption(ThemeMode.SYSTEM, themeMode, stringResource(Res.string.theme_system), onThemeModeSelected)
            ThemeModeOption(ThemeMode.LIGHT, themeMode, stringResource(Res.string.theme_light), onThemeModeSelected)
            ThemeModeOption(ThemeMode.DARK, themeMode, stringResource(Res.string.theme_dark), onThemeModeSelected)

            // 退出登录（已登录时才显示）
            if (isLoggedIn) {
                Spacer(Modifier.height(16.dp))
                Divider()
                Spacer(Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.logout),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeModeOption(
    mode: ThemeMode,
    selectedMode: ThemeMode,
    label: String,
    onSelected: (ThemeMode) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected(mode) }
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selectedMode == mode, onClick = { onSelected(mode) })
        Text(text = label, modifier = Modifier.padding(start = 8.dp))
    }
}
