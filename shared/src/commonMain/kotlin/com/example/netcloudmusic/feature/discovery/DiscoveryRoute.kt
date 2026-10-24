package com.example.netcloudmusic.feature.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.feature.discovery.component.DiscoveryContentView
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.message
import netcloudmusic.shared.generated.resources.search_tip
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 发现界面入口
 */
@Composable
fun DiscoveryRoute(
    toProductDetail: (String) -> Unit = {},
    toUri: (String) -> Unit = {},
    toSearch: () -> Unit = {},
    toggleDrawer: () -> Unit = {},
    viewModel: DiscoveryViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                DiscoveryViewModel(
                    AppContainerProvider.instance.commonRepository,
                    AppContainerProvider.instance.productRepository
                )
            }
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    DiscoveryScreen(
        uiState = uiState,
        toUri = toUri,
        toProductDetail = toProductDetail,
        loadData = viewModel::loadData,
        clearError = viewModel::clearError,
        loadMore = viewModel::loadMore,
        toSearch = toSearch,
        toggleDrawer = toggleDrawer,
    )
}

/**
 * 发现界面骨架
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    uiState: DiscoveryUiState = DiscoveryUiState(),
    toggleDrawer: () -> Unit = {},
    toSearch: () -> Unit = {},
    toProductDetail: (String) -> Unit = {},
    toUri: (String) -> Unit = {},
    loadData: () -> Unit = {},
    loadMore: () -> Unit = {},
    clearError: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            DiscoveryTopBar(toggleDrawer, toSearch)
        },
        // 排除底部导航栏边距
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars),
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            DiscoveryContentView(
                uiState = uiState,
                toUri = toUri,
                loadData = loadData,
                loadMore = loadMore,
                toProductDetail = toProductDetail,
                clearError = clearError,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoveryTopBar(toggleDrawer: () -> Unit, toSearch: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = toggleDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                )
            }
        },
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { toSearch() },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = stringResource(Res.string.search_tip),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        },
        actions = {
            Icon(
                painter = painterResource(Res.drawable.message),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = SpaceExtraMedium)
                    .size(28.dp),
            )
        },
    )
}
