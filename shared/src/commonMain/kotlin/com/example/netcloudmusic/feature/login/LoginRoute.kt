package com.example.netcloudmusic.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.di.AppContainerProvider
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.cancel
import netcloudmusic.shared.generated.resources.confirm
import netcloudmusic.shared.generated.resources.enter_password
import netcloudmusic.shared.generated.resources.enter_phone_or_email
import netcloudmusic.shared.generated.resources.forgot_password
import netcloudmusic.shared.generated.resources.login
import netcloudmusic.shared.generated.resources.login_error
import netcloudmusic.shared.generated.resources.password
import netcloudmusic.shared.generated.resources.phone_or_email
import netcloudmusic.shared.generated.resources.register
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginRoute(
    toBack: () -> Unit,
    finishAllLoginPages: () -> Unit,
    toRegister: () -> Unit = {},
    toSetPassword: () -> Unit = {},
) {
    val viewModel: LoginViewModel = viewModel(
        factory = viewModelFactory {
            initializer { LoginViewModel(AppContainerProvider.instance.userRepository) }
        },
    )
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state) {
        if (state is LoginUiState.Success) finishAllLoginPages()
    }
    LoginScreen(
        uiState = state,
        toBack = toBack,
        toRegister = toRegister,
        toSetPassword = toSetPassword,
        loginClick = viewModel::onLoginClick,
        clearError = viewModel::clearError,
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState = LoginUiState.None,
    toBack: () -> Unit = {},
    toRegister: () -> Unit = {},
    toSetPassword: () -> Unit = {},
    loginClick: (String, String) -> Unit = { _, _ -> },
    clearError: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var username by rememberSaveable { mutableStateOf("13141111666") }
    var password by rememberSaveable { mutableStateOf("ixueaedu") }
    val loading = uiState is LoginUiState.Loading

    if (uiState is LoginUiState.Error) {
        AlertDialog(
            onDismissRequest = clearError,
            title = { Text(stringResource(Res.string.login_error)) },
            text = { Text(uiState.message) },
            confirmButton = {
                TextButton(onClick = clearError) { Text(stringResource(Res.string.confirm)) }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.login)) },
                navigationIcon = {
                    IconButton(onClick = toBack, enabled = !loading) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            UserTextField(
                value = username,
                onValueChanged = { username = it },
                enabled = !loading,
            )
            PasswordTextField(
                value = password,
                onValueChanged = { password = it },
                enabled = !loading,
                loginClick = {
                    keyboardController?.hide()
                    loginClick(username, password)
                },
            )
            Button(
                onClick = {
                    keyboardController?.hide()
                    loginClick(username, password)
                },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text(stringResource(Res.string.login))
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextButton(
                    onClick = toRegister,
                    enabled = !loading,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                ) { Text(stringResource(Res.string.register)) }
                TextButton(
                    onClick = toSetPassword,
                    enabled = !loading,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                ) { Text(stringResource(Res.string.forgot_password)) }
            }
        }
    }
}

@Composable
private fun UserTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var focused by remember { mutableStateOf(false) }
    OutlinedTextField(
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
        trailingIcon = {
            if (focused && value.isNotEmpty()) {
                IconButton(onClick = { onValueChanged("") }) {
                    Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(24.dp))
                }
            }
        },
        value = value,
        onValueChange = onValueChanged,
        label = { Text(stringResource(Res.string.phone_or_email)) },
        placeholder = { Text(stringResource(Res.string.enter_phone_or_email)) },
        singleLine = true,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        modifier = modifier.fillMaxWidth().onFocusChanged { focused = it.isFocused },
    )
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    loginClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var visible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        if (visible) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        },
        value = value,
        enabled = enabled,
        onValueChange = { input ->
            if (input.contains('\n')) loginClick() else onValueChanged(input)
        },
        label = { Text(stringResource(Res.string.password)) },
        placeholder = { Text(stringResource(Res.string.enter_password)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            loginClick()
        }),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier.fillMaxWidth(),
    )
}
