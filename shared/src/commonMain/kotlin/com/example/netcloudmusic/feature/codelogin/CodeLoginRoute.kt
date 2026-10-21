package com.example.netcloudmusic.feature.codelogin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.di.AppContainerProvider
import kotlinx.coroutines.flow.emptyFlow
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.cancel
import netcloudmusic.shared.generated.resources.code_login
import netcloudmusic.shared.generated.resources.login
import netcloudmusic.shared.generated.resources.login_error
import netcloudmusic.shared.generated.resources.confirm
import org.jetbrains.compose.resources.stringResource

@Composable
fun CodeLoginRoute(
    toBack: () -> Unit,
    finishAllLoginPages: () -> Unit,
) {
    val viewModel: CodeLoginViewModel = viewModel(
        factory = viewModelFactory {
            initializer { CodeLoginViewModel(AppContainerProvider.instance.userRepository) }
        },
    )
    val uiState by viewModel.uiState.collectAsState()
    val sendCodeState by viewModel.sendCodeState.collectAsState()
    val data by viewModel.data.collectAsState()

    CodeLoginScreen(
        uiState = uiState,
        sendCodeState = sendCodeState,
        data = data,
        toBack = toBack,
        finishAllLoginPages = finishAllLoginPages,
        onValueChange = viewModel::onValueChange,
        onSendCodeClick = viewModel::onSendCodeClick,
        onLoginClick = viewModel::onLoginClick,
        clearError = viewModel::clearError,
        resetUiState = viewModel::resetUiState,
        sendCodeToastFlow = viewModel.sendCodeToast,
    )
}

@Composable
fun CodeLoginScreen(
    uiState: CodeLoginUiState = CodeLoginUiState.None,
    sendCodeState: SendCodeUiState = SendCodeUiState.Idle,
    /** 发送验证码成功的一次性 Toast 事件流 */
    sendCodeToastFlow: kotlinx.coroutines.flow.Flow<String> = emptyFlow(),
    data: CodeLoginUiModel = CodeLoginUiModel(),
    toBack: () -> Unit = {},
    finishAllLoginPages: () -> Unit = {},
    onValueChange: (CodeLoginUiModel) -> Unit = {},
    onSendCodeClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    clearError: () -> Unit = {},
    resetUiState: () -> Unit = {},
    @Suppress("UNUSED_PARAMETER") consumeSendCodeToast: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading = uiState is CodeLoginUiState.Loading

    // ── 登录错误 AlertDialog（与 Compose 版一致）──────────────────────────────
    if (uiState is CodeLoginUiState.Error) {
        AlertDialog(
            onDismissRequest = clearError,
            title = { Text(stringResource(Res.string.login_error)) },
            text = { Text(uiState.message) },
            confirmButton = {
                TextButton(onClick = clearError) {
                    Text(stringResource(Res.string.confirm))
                }
            },
        )
    }

    // ── 发送成功 Toast：持续 collect Flow，不会被 key 变化取消 ───────────────
    LaunchedEffect(Unit) {
        sendCodeToastFlow.collect { raw ->
            val parts = raw.split("|")
            val phone = parts[0]
            val code = parts.getOrNull(1)
            val msg = buildString {
                append("验证码已发送到 $phone")
                code?.let { append("，验证码：$it") }
            }
            snackbarHostState.showSnackbar(msg)
        }
    }

    // ── 登录成功跳转 ──────────────────────────────────────────────────────────
    LaunchedEffect(uiState) {
        if (uiState is CodeLoginUiState.Success) finishAllLoginPages()
    }

    // ── 发送失败 Toast ────────────────────────────────────────────────────────
    LaunchedEffect(sendCodeState) {
        if (sendCodeState is SendCodeUiState.Error) {
            snackbarHostState.showSnackbar(sendCodeState.message ?: "发送验证码失败")
        }
    }

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                title = stringResource(Res.string.code_login),
                toBack = { if (!isLoading) toBack() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.code_login),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 20.dp),
                )

                // ── 手机号输入框（带 leadingIcon + 聚焦时显示清除按钮）──────────
                PhoneTextField(
                    value = data.phone,
                    onValueChange = { onValueChange(data.copy(phone = it)) },
                    enabled = !isLoading,
                )

                // ── 验证码输入框 + 发送按钮行 ─────────────────────────────────
                CodeInputRow(
                    code = data.code,
                    onCodeChange = { if (it.length <= 6) onValueChange(data.copy(code = it)) },
                    sendCodeState = sendCodeState,
                    enabled = !isLoading,
                    onSendCodeClick = {
                        keyboardController?.hide()
                        onSendCodeClick()
                    },
                    onDone = {
                        keyboardController?.hide()
                        onLoginClick()
                    },
                )

                // ── 登录按钮 ──────────────────────────────────────────────────
                Button(
                    onClick = {
                        keyboardController?.hide()
                        onLoginClick()
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text(stringResource(Res.string.login))
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 子组件
// ─────────────────────────────────────────────────────────────────────────────

/** 手机号输入框：带手机图标，聚焦且有内容时显示清除按钮 */
@Composable
private fun PhoneTextField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    var focused by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("手机号") },
        placeholder = { Text("请输入手机号") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (focused && value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        },
        singleLine = true,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
        ),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focused = it.isFocused },
    )
}

/** 验证码输入框 + 发送按钮（含发送中 Loading、倒计时显示） */
@Composable
private fun CodeInputRow(
    code: String,
    onCodeChange: (String) -> Unit,
    sendCodeState: SendCodeUiState,
    enabled: Boolean,
    onSendCodeClick: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            label = { Text("验证码") },
            placeholder = { Text("请输入6位验证码") },
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            modifier = Modifier.weight(1f),
        )

        val isSending = sendCodeState is SendCodeUiState.Sending
        val isCountdown = sendCodeState is SendCodeUiState.Countdown

        OutlinedButton(
            onClick = onSendCodeClick,
            enabled = !isSending && !isCountdown && enabled,
        ) {
            when (sendCodeState) {
                is SendCodeUiState.Sending -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                    )
                }
                is SendCodeUiState.Countdown -> {
                    Text(
                        text = "${sendCodeState.remainSeconds}s后重发",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
                is SendCodeUiState.Error -> Text("重新发送")
                else -> Text("发送验证码")
            }
        }
    }
}
