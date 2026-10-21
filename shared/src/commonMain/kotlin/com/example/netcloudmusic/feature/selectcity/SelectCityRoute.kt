package com.example.netcloudmusic.feature.selectcity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.di.AppContainerProvider

@Composable
fun SelectCityRoute(toBack: () -> Unit) {
    val viewModel: SelectCityViewModel = viewModel(
        factory = viewModelFactory {
            initializer { SelectCityViewModel(AppContainerProvider.instance.cityRepository) }
        },
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                title = "选择城市",
                toBack = { if (viewModel.canBack()) toBack() else viewModel.goBack() },
            )
        },
    ) { paddingValues ->
        when (val state = uiState) {
            is SelectCityUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                ) {
                    items(state.items, key = { it.code }) { item ->
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    val done = viewModel.onCitySelected(item)
                                    if (done) {
                                        val selected = viewModel.getSelected()
                                        val province = selected.getOrNull(0) ?: return@clickable
                                        val city     = selected.getOrNull(1) ?: return@clickable
                                        val area     = selected.getOrNull(2) ?: return@clickable
                                        // 触发桥接回调，把结果送回给 addressdetail 页
                                        SelectCityCallback.onResult?.invoke(
                                            province.name, province.code,
                                            city.name,     city.code,
                                            area.name,     area.code,
                                        )
                                        SelectCityCallback.onResult = null
                                        toBack()
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                        )
                    }
                }
            }
            else -> {}
        }
    }
}
