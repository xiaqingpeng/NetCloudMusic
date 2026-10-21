package com.example.netcloudmusic.feature.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.di.AppContainerProvider
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.cancel
import netcloudmusic.shared.generated.resources.confirm
import netcloudmusic.shared.generated.resources.register
import org.jetbrains.compose.resources.stringResource

/**
 * 注册页入口
 */
@Composable
fun RegisterRoute(
    toBack: () -> Unit,
    finishAllLoginPages: () -> Unit = {},
) {
    val viewModel: RegisterViewModel = viewModel(
        factory = viewModelFactory {
            initializer { RegisterViewModel(AppContainerProvider.instance.userRepository) }
        },
    )
    val uiState by viewModel.uiState.collectAsState()
    val data by viewModel.data.collectAsState()

    RegisterScreen(
        uiState = uiState,
        data = data,
        onValueChange = viewModel::onValueChange,
        toBack = toBack,
        finishAllLoginPages = finishAllLoginPages,
        onRegisterClick = viewModel::onRegisterClick,
        resetUiState = viewModel::resetUiState,
    )
}

@Composable
fun RegisterScreen(
    uiState: RegisterUiState = RegisterUiState.None,
    data: RegisterUiModel = RegisterUiModel(),
    onValueChange: (RegisterUiModel) -> Unit = {},
    toBack: () -> Unit = {},
    finishAllLoginPages: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    resetUiState: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // 处理 UI 状态
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is RegisterUiState.Success -> finishAllLoginPages()
            is RegisterUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                resetUiState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            MyCenterTopAppBar(title = stringResource(Res.string.register), toBack = toBack)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            // 昵称
            OutlinedTextField(
                value = data.nickname,
                onValueChange = { onValueChange(data.copy(nickname = it)) },
                label = { Text("昵称") },
                placeholder = { Text("请输入昵称") },
                singleLine = true,
                enabled = uiState !is RegisterUiState.Loading,
                modifier = Modifier.fillMaxWidth(),
            )
            // 手机号
            OutlinedTextField(
                value = data.phone,
                onValueChange = { onValueChange(data.copy(phone = it)) },
                label = { Text("手机号") },
                placeholder = { Text("请输入手机号") },
                singleLine = true,
                enabled = uiState !is RegisterUiState.Loading,
                modifier = Modifier.fillMaxWidth(),
            )
            // 邮箱
            OutlinedTextField(
                value = data.email,
                onValueChange = { onValueChange(data.copy(email = it)) },
                label = { Text("邮箱") },
                placeholder = { Text("请输入邮箱") },
                singleLine = true,
                enabled = uiState !is RegisterUiState.Loading,
                modifier = Modifier.fillMaxWidth(),
            )
            // 密码
            OutlinedTextField(
                value = data.password,
                onValueChange = { onValueChange(data.copy(password = it)) },
                label = { Text("密码") },
                placeholder = { Text("请输入密码（至少6位）") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                enabled = uiState !is RegisterUiState.Loading,
                modifier = Modifier.fillMaxWidth(),
            )
            // 确认密码
            OutlinedTextField(
                value = data.confirmPassword,
                onValueChange = { onValueChange(data.copy(confirmPassword = it)) },
                label = { Text("确认密码") },
                placeholder = { Text("请再次输入密码") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                enabled = uiState !is RegisterUiState.Loading,
                modifier = Modifier.fillMaxWidth(),
            )
            // 注册按钮
            Button(
                onClick = onRegisterClick,
                enabled = uiState !is RegisterUiState.Loading,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                if (uiState is RegisterUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text("注册并登录")
                }
            }
        }
    }
}
