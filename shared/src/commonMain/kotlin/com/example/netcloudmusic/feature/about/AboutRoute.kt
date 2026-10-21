package com.example.netcloudmusic.feature.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
// 删除不再使用的 Box、Alignment import（由编译器提示即可，这里清理掉）
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.design.component.MyLoading
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.di.AppContainerProvider
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.splash_logo
import org.jetbrains.compose.resources.painterResource

/**
 * 关于页面（KMP 共享层）
 * WorkManager 下载等 Android 专有功能由 androidApp 宿主层扩展。
 */
@Composable
fun AboutRoute(toBack: () -> Unit) {
    val viewModel: AboutViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                AboutViewModel(
                    context = AppContainerProvider.appContext,
                    versionName = AppContainerProvider.versionName,
                    versionCode = AppContainerProvider.versionCode,
                )
            }
        },
    )
    val uiState by viewModel.uiState.collectAsState()
    AboutScreen(
        uiState = uiState,
        toBack = toBack,
        onClearCacheClick = viewModel::onClearCacheClick,
    )
}

@Composable
fun AboutScreen(
    uiState: AboutUiState = AboutUiState.Loading,
    toBack: () -> Unit = {},
    onClearCacheClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(title = "关于", toBack = toBack)
        },
    ) { paddingValues ->
        when (uiState) {
            AboutUiState.Loading -> MyLoading(Modifier.fillMaxSize().padding(paddingValues))
            is AboutUiState.Success -> {
                AboutContent(
                    uiState = uiState,
                    onClearCacheClick = onClearCacheClick,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}

@Composable
private fun AboutContent(
    uiState: AboutUiState.Success,
    onClearCacheClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showClearCacheDialog by remember { mutableStateOf(false) }

    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            title = { Text("清除缓存") },
            text = { Text("确认清除所有缓存（${uiState.cacheSize}）？") },
            confirmButton = {
                TextButton(onClick = {
                    showClearCacheDialog = false
                    onClearCacheClick()
                }) { Text("确认") }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) { Text("取消") }
            },
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Image(
            painter = painterResource(Res.drawable.splash_logo),
            contentDescription = null,
            modifier = Modifier.padding(top = 48.dp).size(100.dp),
        )
        Spacer(Modifier.size(32.dp))

        // 当前版本（不可点击，纯展示）
        AboutSettingRow(
            title = "当前版本",
            value = if (uiState.versionName.isNotEmpty())
                "v${uiState.versionName} (${uiState.versionCode})" else "—",
        )

        // 清除缓存
        AboutSettingRow(
            title = "清除缓存",
            value = if (uiState.isClearingCache) "清除中..." else uiState.cacheSize,
            onClick = { if (!uiState.isClearingCache) showClearCacheDialog = true },
        )

        // 功能介绍（暂无独立页面，预留）
        AboutSettingRow(
            title = "功能介绍",
            value = "",
            onClick = { /* TODO: 跳转功能介绍页面 */ },
        )

        // 关于我们（暂无独立页面，预留）
        AboutSettingRow(
            title = "关于我们",
            value = "",
            onClick = { /* TODO: 跳转关于我们页面 */ },
        )
    }
}

@Composable
private fun AboutSettingRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.5f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.outline,
        )
    }
}
