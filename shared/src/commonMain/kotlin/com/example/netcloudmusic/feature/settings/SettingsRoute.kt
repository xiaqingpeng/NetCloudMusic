package com.example.netcloudmusic.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.data.repository.ThemeMode
import com.example.netcloudmusic.core.di.AppContainerProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.theme_dark
import netcloudmusic.shared.generated.resources.theme_light
import netcloudmusic.shared.generated.resources.theme_mode
import netcloudmusic.shared.generated.resources.theme_system
import netcloudmusic.shared.generated.resources.settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsRoute(toBack: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel(
        factory = viewModelFactory {
            initializer { SettingsViewModel(AppContainerProvider.instance.themeRepository) }
        },
    )
    val state by viewModel.uiState.collectAsState()

    SettingsScreen(
        themeMode = state.themeMode,
        toBack = toBack,
        onThemeModeSelected = viewModel::setThemeMode,
    )
}

@Composable
private fun SettingsScreen(
    themeMode: ThemeMode,
    toBack: () -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = toBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Text(
                text = stringResource(Res.string.theme_mode),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            )
            ThemeModeOption(
                mode = ThemeMode.SYSTEM,
                selectedMode = themeMode,
                label = stringResource(Res.string.theme_system),
                onSelected = onThemeModeSelected,
            )
            ThemeModeOption(
                mode = ThemeMode.LIGHT,
                selectedMode = themeMode,
                label = stringResource(Res.string.theme_light),
                onSelected = onThemeModeSelected,
            )
            ThemeModeOption(
                mode = ThemeMode.DARK,
                selectedMode = themeMode,
                label = stringResource(Res.string.theme_dark),
                onSelected = onThemeModeSelected,
            )
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
        RadioButton(
            selected = selectedMode == mode,
            onClick = { onSelected(mode) },
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}
