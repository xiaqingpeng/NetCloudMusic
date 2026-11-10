package com.example.netcloudmusic.feature.addressdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.di.AppContainerProvider

@Composable
fun AddressDetailRoute(
    addressId: String,               // "" = 新增
    toBack: () -> Unit,
    /** 点击省市区行时调用。城市选完后通过 onCityResult 把三级结果回传给本页 */
    toSelectCity: (onCityResult: (province: String, provinceCode: String, city: String, cityCode: String, area: String, areaCode: String) -> Unit) -> Unit,
    onSaveSuccess: () -> Unit = {},
) {
    val viewModel: AddressDetailViewModel = viewModel(
        key = addressId,
        factory = viewModelFactory {
            initializer {
                AddressDetailViewModel(
                    addressId = addressId,
                    addressRepository = AppContainerProvider.instance.addressRepository,
                )
            }
        },
    )
    val uiState by viewModel.uiState.collectAsState()
    val data by viewModel.data.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val s = uiState) {
            is AddressDetailUiState.Success -> {
                onSaveSuccess()
                toBack()
            }
            is AddressDetailUiState.Error -> {
                snackbarHostState.showSnackbar(s.message ?: "操作失败")
                viewModel.resetUiState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                title = if (viewModel.isNewAddress) "新增地址" else "编辑地址",
                toBack = toBack,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues),
        ) {
            val loading = uiState is AddressDetailUiState.Loading

            AddressInputText(
                title = "收货人",
                placeholder = "请输入收货人姓名",
                value = data.name,
                onValueChange = { viewModel.onValueChange(data.copy(name = it)) },
                enabled = !loading,
            )
            AddressInputText(
                title = "手机号",
                placeholder = "请输入手机号",
                value = data.telephone,
                onValueChange = { viewModel.onValueChange(data.copy(telephone = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                enabled = !loading,
            )
            AddressInputSelect(
                title = "省市区",
                placeholder = "请选择省市区",
                value = data.areaDisplay,
                onClick = {
                    toSelectCity { province, provinceCode, city, cityCode, area, areaCode ->
                        viewModel.onCitySelected(province, provinceCode, city, cityCode, area, areaCode)
                    }
                },
                enabled = !loading,
            )
            AddressInputText(
                title = "详细地址",
                placeholder = "请输入详细地址",
                value = data.detail,
                onValueChange = { viewModel.onValueChange(data.copy(detail = it)) },
                enabled = !loading,
            )
            AddressInputSwitch(
                title = "设为默认",
                value = data.default,
                onValueChange = { viewModel.onValueChange(data.copy(default = it)) },
                enabled = !loading,
                modifier = Modifier.padding(top = 8.dp),
            )

            Button(
                onClick = viewModel::onSaveClick,
                enabled = !loading,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .height(48.dp)
                    .fillMaxWidth(),
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text("保存")
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 通用输入组件
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AddressInputText(
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
) {
    var focused by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = 2.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(start = 16.dp, end = 8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.widthIn(min = 80.dp),
        )
        Box(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    singleLine = true,
                    keyboardOptions = keyboardOptions,
                    enabled = enabled,
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { focused = it.isFocused },
                )
                if (focused && value.isNotEmpty() && enabled) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            }
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Composable
fun AddressInputSelect(
    title: String,
    placeholder: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = 2.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(start = 16.dp, end = 12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.widthIn(min = 80.dp),
        )
        Text(
            text = value.ifBlank { placeholder },
            style = MaterialTheme.typography.bodyLarge,
            color = if (value.isBlank()) MaterialTheme.colorScheme.outline
                    else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        Icon(
            Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.outline,
        )
    }
}

@Composable
fun AddressInputSwitch(
    title: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        Switch(checked = value, onCheckedChange = onValueChange, enabled = enabled)
    }
}
