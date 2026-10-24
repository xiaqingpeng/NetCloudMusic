package com.example.netcloudmusic.feature.forgetpassword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.di.AppContainerProvider
import netcloudmusic.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

private const val CODE_INPUT_MAX = 6

@Composable
fun ForgetPasswordRoute(
    toBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: ForgetPasswordViewModel = viewModel(
        factory = viewModelFactory {
            initializer { 
                ForgetPasswordViewModel(AppContainerProvider.instance.userRepository) 
            }
        }
    ),
) {
    val uiState by viewModel.uiState.collectAsState()
    val sendCodeState by viewModel.sendCodeState.collectAsState()
    val sendCodeToast by viewModel.sendCodeToast.collectAsState()
    val data by viewModel.data.collectAsState()

    ForgetPasswordScreen(
        uiState = uiState,
        sendCodeState = sendCodeState,
        sendCodeToast = sendCodeToast,
        data = data,
        toBack = toBack,
        onSuccess = onSuccess,
        onValueChange = viewModel::onValueChange,
        onSendCodeClick = viewModel::onSendCodeClick,
        onConfirmClick = viewModel::onConfirmClick,
        clearError = viewModel::clearError,
        consumeSendCodeToast = viewModel::consumeSendCodeToast,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordScreen(
    uiState: ForgetPasswordUiState = ForgetPasswordUiState.None,
    sendCodeState: ForgetSendCodeUiState = ForgetSendCodeUiState.Idle,
    sendCodeToast: String? = null,
    data: ForgetPasswordUiModel = ForgetPasswordUiModel(),
    toBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onValueChange: (ForgetPasswordUiModel) -> Unit = {},
    onSendCodeClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    clearError: () -> Unit = {},
    consumeSendCodeToast: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // 成功后导航
    LaunchedEffect(uiState) {
        if (uiState is ForgetPasswordUiState.Success) {
            onSuccess()
        }
    }

    // Toast 提示
    LaunchedEffect(sendCodeToast) {
        sendCodeToast?.let {
            snackbarHostState.showSnackbar(it)
            consumeSendCodeToast()
        }
    }

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                toBack = toBack,
                title = stringResource(Res.string.forgot_password),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 手机号输入
            OutlinedTextField(
                value = data.phone,
                onValueChange = { onValueChange(data.copy(phone = it)) },
                label = { Text(stringResource(Res.string.phone)) },
                placeholder = { Text(stringResource(Res.string.enter_phone)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                ),
                trailingIcon = {
                    if (data.phone.isNotEmpty()) {
                        IconButton(onClick = { onValueChange(data.copy(phone = "")) }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // 验证码输入和发送按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = data.code,
                    onValueChange = { 
                        if (it.length <= CODE_INPUT_MAX) onValueChange(data.copy(code = it)) 
                    },
                    label = { Text(stringResource(Res.string.verification_code)) },
                    placeholder = { Text(stringResource(Res.string.enter_code)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.weight(1f)
                )

                val isSending = sendCodeState is ForgetSendCodeUiState.Sending
                val isCountdown = sendCodeState is ForgetSendCodeUiState.Countdown

                OutlinedButton(
                    onClick = onSendCodeClick,
                    enabled = !isSending && !isCountdown,
                    modifier = Modifier.height(56.dp)
                ) {
                    when (sendCodeState) {
                        ForgetSendCodeUiState.Sending -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                            )
                        }
                        is ForgetSendCodeUiState.Countdown -> {
                            Text("${sendCodeState.remainSeconds}s")
                        }
                        else -> {
                            Text(stringResource(Res.string.send_code))
                        }
                    }
                }
            }

            // 新密码
            OutlinedTextField(
                value = data.newPassword,
                onValueChange = { onValueChange(data.copy(newPassword = it)) },
                label = { Text(stringResource(Res.string.password)) },
                placeholder = { Text(stringResource(Res.string.enter_password)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // 确认密码
            OutlinedTextField(
                value = data.confirmPassword,
                onValueChange = { onValueChange(data.copy(confirmPassword = it)) },
                label = { Text(stringResource(Res.string.confirm_password)) },
                placeholder = { Text(stringResource(Res.string.enter_confirm_password)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // 确认按钮
            Button(
                onClick = onConfirmClick,
                enabled = uiState !is ForgetPasswordUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState is ForgetPasswordUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(stringResource(Res.string.confirm))
                }
            }
        }
    }

    // 错误提示 Snackbar
    LaunchedEffect(uiState) {
        if (uiState is ForgetPasswordUiState.ErrorMessage) {
            snackbarHostState.showSnackbar(uiState.message)
            clearError()
        }
    }

    // 错误提示对话框
    if (uiState is ForgetPasswordUiState.Error) {
        AlertDialog(
            onDismissRequest = clearError,
            title = { Text("错误") },
            text = { Text(uiState.throwable?.message ?: "未知错误") },
            confirmButton = {
                TextButton(onClick = clearError) {
                    Text(stringResource(Res.string.confirm))
                }
            }
        )
    }
}
