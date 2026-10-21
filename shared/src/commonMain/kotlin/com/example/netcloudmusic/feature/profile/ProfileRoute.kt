package com.example.netcloudmusic.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.design.component.MyErrorView
import com.example.netcloudmusic.core.design.component.MyLoading
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.model.User
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.cancel
import netcloudmusic.shared.generated.resources.confirm
import netcloudmusic.shared.generated.resources.logout
import netcloudmusic.shared.generated.resources.unknown_error
import org.jetbrains.compose.resources.stringResource

/**
 * 个人资料页入口
 */
@Composable
fun ProfileRoute(
    toBack: () -> Unit,
    toSelectCity: (onCityResult: (String, String, String, String, String, String) -> Unit) -> Unit = {},
) {
    val viewModel: ProfileViewModel = viewModel(
        factory = viewModelFactory {
            initializer { ProfileViewModel(AppContainerProvider.instance.userRepository) }
        },
    )
    val uiState by viewModel.uiState.collectAsState()
    val data by viewModel.data.collectAsState()
    val isShowGenderDialog by viewModel.isShowGenderDialog.collectAsState()
    val isShowConfirmLogoutDialog by viewModel.isShowConfirmLogoutDialog.collectAsState()
    val toast by viewModel.toast.collectAsState()
    val navigateBack by viewModel.navigateBack.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Toast 一次性事件
    LaunchedEffect(toast) {
        val event = toast ?: return@LaunchedEffect
        viewModel.consumeToast()
        when (event) {
            is ProfileToastEvent.Text -> snackbarHostState.showSnackbar(event.message)
        }
    }

    // 保存成功或退出后返回
    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            viewModel.consumeNavigateBack()
            toBack()
        }
    }

    ProfileScreen(
        uiState = uiState,
        data = data,
        isShowGenderDialog = isShowGenderDialog,
        isShowConfirmLogoutDialog = isShowConfirmLogoutDialog,
        snackbarHostState = snackbarHostState,
        toBack = toBack,
        onUserValueChanged = viewModel::onUserValueChanged,
        showGenderDialog = viewModel::showGenderDialog,
        dismissGenderDialog = viewModel::dismissGenderDialog,
        dismissConfirmLogoutDialog = viewModel::dismissConfirmLogoutDialog,
        onLogoutClick = viewModel::showConfirmLogoutDialog,
        onSaveClick = viewModel::onSaveClick,
        confirmLogout = viewModel::confirmLogout,
        toSelectCity = { toSelectCity { p, pc, c, cc, a, ac ->
            viewModel.onCitySelected(p, pc, c, cc, a, ac)
        } },
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState = ProfileUiState.Loading,
    data: User = User(),
    isShowGenderDialog: Boolean = false,
    isShowConfirmLogoutDialog: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    toBack: () -> Unit = {},
    onUserValueChanged: (User) -> Unit = {},
    showGenderDialog: () -> Unit = {},
    dismissGenderDialog: () -> Unit = {},
    dismissConfirmLogoutDialog: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    confirmLogout: () -> Unit = {},
    toSelectCity: (() -> Unit) = {},
) {
    val isSaving = uiState == ProfileUiState.Saving
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                title = "个人资料",
                toBack = toBack,
                actions = {
                    TextButton(onClick = onSaveClick, enabled = !isSaving) {
                        Text(if (isSaving) "保存中..." else "保存")
                    }
                },
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
            when (uiState) {
                ProfileUiState.Loading -> MyLoading(Modifier.fillMaxSize())
                is ProfileUiState.Error -> MyErrorView(
                    message = uiState.message ?: stringResource(Res.string.unknown_error),
                    modifier = Modifier.fillMaxSize(),
                )
                ProfileUiState.Success, ProfileUiState.Saving -> {
                    ProfileContentView(
                        data = data,
                        onUserValueChanged = onUserValueChanged,
                        showGenderDialog = showGenderDialog,
                        onLogoutClick = onLogoutClick,
                        toSelectCity = toSelectCity,
                    )
                }
            }
        }
    }

    // 性别选择弹窗
    if (isShowGenderDialog) {
        GenderDialog(
            currentGender = data.gender,
            onSelect = { gender ->
                onUserValueChanged(data.copy(gender = gender))
                dismissGenderDialog()
            },
            onDismiss = dismissGenderDialog,
        )
    }

    // 退出登录确认弹窗
    if (isShowConfirmLogoutDialog) {
        AlertDialog(
            onDismissRequest = dismissConfirmLogoutDialog,
            title = { Text(stringResource(Res.string.logout)) },
            text = { Text("确认退出登录？") },
            confirmButton = {
                TextButton(onClick = confirmLogout) { Text(stringResource(Res.string.confirm)) }
            },
            dismissButton = {
                TextButton(onClick = dismissConfirmLogoutDialog) { Text(stringResource(Res.string.cancel)) }
            },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 内容区域
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProfileContentView(
    data: User,
    onUserValueChanged: (User) -> Unit,
    showGenderDialog: () -> Unit,
    onLogoutClick: () -> Unit,
    toSelectCity: () -> Unit = {},
) {
    // 头像（只读展示，KMP 暂不支持图片选择器）
    ProfileSettingRow {
        ProfileSettingTitle("头像", Modifier.weight(1f))
        AsyncImage(
            model = data.icon,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .size(64.dp)
                .clip(CircleShape),
        )
    }

    // 背景图（只读展示，KMP 暂不支持图片选择器）
    ProfileSettingRow {
        ProfileSettingTitle("背景图", Modifier.weight(1f))
        AsyncImage(
            model = data.bg,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .size(width = 80.dp, height = 48.dp)
                .clip(MaterialTheme.shapes.small),
        )
    }

    // 昵称（可编辑）
    ProfileSettingRow(modifier = Modifier.padding(top = 8.dp)) {
        ProfileSettingTitle("昵称", Modifier.weight(1f))
        BasicTextField(
            value = data.nickname ?: "",
            onValueChange = { onUserValueChanged(data.copy(nickname = it)) },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.outline,
            ),
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
    }

    // 性别（点击弹窗选择）
    ProfileSettingValue(
        title = "性别",
        value = data.getGenderFormat(),
        modifier = Modifier.clickable { showGenderDialog() },
    )

    // 生日（可编辑）
    ProfileSettingRow {
        ProfileSettingTitle("生日", Modifier.weight(1f))
        BasicTextField(
            value = data.birthday ?: "",
            onValueChange = { onUserValueChanged(data.copy(birthday = it)) },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.outline,
            ),
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
    }

    // 手机号（只读）
    ProfileSettingValue(title = "手机号", value = data.phone ?: "")

    // 邮箱（只读）
    ProfileSettingValue(title = "邮箱", value = data.email ?: "")

    // 地区（点击进入城市选择器）
    val areaDisplay = when {
        data.province.isNullOrEmpty() -> ""
        data.province == data.city    -> "${data.province}${data.area}"
        else                          -> "${data.province}${data.city}${data.area}"
    }
    ProfileSettingValue(
        title = "地区",
        value = areaDisplay.ifEmpty { "请选择" },
        modifier = Modifier.clickable { toSelectCity() },
    )

    // 个人介绍标题
    ProfileSettingRow(modifier = Modifier.padding(top = 8.dp)) {
        ProfileSettingTitle("个人介绍", Modifier.weight(1f))
    }

    // 个人介绍输入框
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        BasicTextField(
            value = data.detail ?: "",
            onValueChange = { onUserValueChanged(data.copy(detail = it)) },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
        )
        if (data.detail.isNullOrEmpty()) {
            Text(
                text = "填写个人介绍",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }

    // 退出登录按钮
    OutlinedButton(
        onClick = onLogoutClick,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 40.dp)
            .fillMaxWidth(),
    ) {
        Text(stringResource(Res.string.logout))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 通用设置行组件
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProfileSettingValue(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = 2.dp)
            .fillMaxWidth()
            .heightIn(min = 50.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp),
    ) {
        ProfileSettingTitle(title, Modifier.weight(0.5f))
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

@Composable
private fun ProfileSettingRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = 2.dp)
            .fillMaxWidth()
            .heightIn(min = 50.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp),
    ) { content() }
}

@Composable
private fun ProfileSettingTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// 性别选择弹窗
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun GenderDialog(
    currentGender: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择性别") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("0" to "保密", "10" to "男", "20" to "女").forEach { (value, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(value) }
                            .padding(vertical = 4.dp),
                    ) {
                        RadioButton(
                            selected = currentGender == value,
                            onClick = { onSelect(value) },
                        )
                        Text(label, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(Res.string.cancel)) }
        },
    )
}
