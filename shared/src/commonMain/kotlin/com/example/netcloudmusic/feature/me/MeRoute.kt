package com.example.netcloudmusic.feature.me

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.theme.LocalDividerColor
import com.example.netcloudmusic.core.design.theme.SpaceExtraOuter
import com.example.netcloudmusic.core.design.theme.SpaceExtraSmall
import com.example.netcloudmusic.core.design.theme.SpaceLarge
import com.example.netcloudmusic.core.design.theme.SpaceMediumHeight
import com.example.netcloudmusic.core.design.theme.SpaceSmallWidth
import com.example.netcloudmusic.core.design.theme.smallTopRoundedCornerShape
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.model.User
import com.example.netcloudmusic.feature.me.model.ButtonControl
import com.example.netcloudmusic.feature.me.model.OrderButtonItem
import com.example.netcloudmusic.core.util.ResourceUtil
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.default_avatar
import netcloudmusic.shared.generated.resources.login_or_register
import netcloudmusic.shared.generated.resources.me_fans
import netcloudmusic.shared.generated.resources.me_friend
import netcloudmusic.shared.generated.resources.me_login_tip
import netcloudmusic.shared.generated.resources.scan
import netcloudmusic.shared.generated.resources.unknown_user
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 我的界面
 */
@Composable
fun MeRoute(
    toLogin: () -> Unit = {},
    toGithubProfile: () -> Unit = {},
    toSettings: () -> Unit = {},
    toProfile: () -> Unit = {},
    toReceivingAddress: () -> Unit = {},
    toAbout: () -> Unit = {},
    toOrder: (Int) -> Unit = {},
    toScan: () -> Unit = {},
    toCode: () -> Unit = {},
) {
    val viewModel: MeViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                MeViewModel(
                    AppContainerProvider.instance.userRepository,
                    AppContainerProvider.instance.meRepository,
                    AppContainerProvider.instance.githubRepository,
                )
            }
        },
    )
    val state by viewModel.uiState.collectAsState()
    MeScreen(
        state = state,
        toLogin = toLogin,
        toGithubProfile = toGithubProfile,
        onLogout = viewModel::logout,
        toSettings = toSettings,
        toProfile = toProfile,
        toReceivingAddress = toReceivingAddress,
        toAbout = toAbout,
        toOrder = { style ->
            // 订单接口依赖商城业务 session（Authorization）。
            // GitHub 本地登录只有 userState，没有 NetworkSession，直接进订单会加载失败。
            if (AppContainerProvider.instance.userRepository.hasBusinessSession()) {
                toOrder(style)
            } else {
                toLogin()
            }
        },
        toScan = toScan,
        toCode = toCode,
        orderButtons = viewModel.getOrderButtons(),
        serviceButtons = viewModel.getMoreServiceButtons(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeScreen(
    state: MeUiState = MeUiState(),
    toLogin: () -> Unit = {},
    toGithubProfile: () -> Unit = {},
    onLogout: () -> Unit = {},
    toSettings: () -> Unit = {},
    toProfile: () -> Unit = {},
    toReceivingAddress: () -> Unit = {},
    toAbout: () -> Unit = {},
    toOrder: (Int) -> Unit = {},
    toScan: () -> Unit = {},
    toCode: () -> Unit = {},
    orderButtons: List<OrderButtonItem> = emptyList(),
    serviceButtons: List<ButtonControl> = emptyList(),
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    Image(
                        painter = painterResource(Res.drawable.scan),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(horizontal = SpaceExtraOuter)
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable(onClick = toScan)
                    )
                },
            )
        },
        // 排除底部导航栏内边距，与 Compose 版一致
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars),
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(SpaceExtraOuter),
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(scrollState),
        ) {
            // ── 顶部 primary 背景区域（用户信息 + VIP 提示）
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = SpaceExtraOuter),
            ) {
                if (state.isLoggedIn && state.user != null) {
                    MeUserProfile(
                        user = state.user,
                        toProfile = if (state.user.style == 20) toGithubProfile else toProfile,
                        toCode = toCode,
                    )
                } else {
                    MeDefaultProfile(toLogin = toLogin)
                }
                MeVipHint()
            }

            // ── 统计信息卡片
            MeCountInfo(
                followingsCount = state.user?.followingsCount ?: 0,
                followersCount = state.user?.followersCount ?: 0,
                commentsCount = state.user?.commentsCount ?: 0,
                modifier = Modifier.padding(horizontal = SpaceExtraOuter),
            )

            // ── 我的订单卡片
            MeOrderInfo(
                orderButtons = orderButtons,
                toOrder = toOrder,
                modifier = Modifier.padding(horizontal = SpaceExtraOuter),
            )

            // ── 更多服务按钮卡片
            MeServiceButtons(
                buttons = serviceButtons,
                modifier = Modifier.padding(
                    start = SpaceExtraOuter,
                    end = SpaceExtraOuter,
                    bottom = SpaceExtraOuter,
                ),
                onItemClick = { btn ->
                    when (btn.key) {
                        "setting"           -> toSettings()
                        "receiving_address" -> toReceivingAddress()
                        "about"             -> toAbout()
                        "my_profile"        -> if (state.user?.style == 20) toGithubProfile() else toProfile()
                    }
                },
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 用户信息区
// ─────────────────────────────────────────────────────────────────────────────

/** 已登录：头像 + 昵称 + 扫码 + 箭头，点击跳转个人资料 */
@Composable
private fun MeUserProfile(user: User, toProfile: () -> Unit, toCode: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = toProfile),
    ) {
        AsyncImage(
            model = ResourceUtil.r(user.icon ?: ""),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape),
            error = painterResource(Res.drawable.default_avatar),
            placeholder = painterResource(Res.drawable.default_avatar),
        )
        SpaceSmallWidth()
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.nickname ?: user.id ?: stringResource(Res.string.unknown_user),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            Text(
                text = stringResource(Res.string.me_login_tip),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        IconButton(onClick = toCode) {
            Icon(Icons.Default.QrCode, contentDescription = "我的二维码", tint = Color.White)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp),
        )
    }
}

/** 未登录：默认头像 + 登录/注册 + 箭头 */
@Composable
private fun MeDefaultProfile(toLogin: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = toLogin),
    ) {
        Image(
            painter = painterResource(Res.drawable.default_avatar),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape),
        )
        SpaceSmallWidth()
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.login_or_register),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            Text(
                text = stringResource(Res.string.me_login_tip),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// VIP 提示条（仅上方圆角，对齐 Compose 版的 smallTopRoundedCornerShape）
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun MeVipHint() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 35.dp)
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = smallTopRoundedCornerShape,   // 仅上方两角圆角
            )
            .fillMaxWidth()
            .padding(SpaceLarge),
    ) {
        Text(
            text = "超级会员",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "立即开通",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 统计卡片（关注/粉丝/收藏/评论）
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun MeCountInfo(
    followingsCount: Long,
    followersCount: Long,
    commentsCount: Long,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MeCountItem(
                count = followingsCount.toString(),
                label = stringResource(Res.string.me_friend),
                modifier = Modifier.weight(1f),
            )
            MeCountDivider()
            MeCountItem(
                count = followersCount.toString(),
                label = stringResource(Res.string.me_fans),
                modifier = Modifier.weight(1f),
            )
            MeCountDivider()
            MeCountItem(count = "0", label = "收藏", modifier = Modifier.weight(1f))
            MeCountDivider()
            MeCountItem(count = commentsCount.toString(), label = "评论", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun MeCountItem(count: String, label: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = SpaceLarge),
    ) {
        Text(count, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        SpaceMediumHeight()
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun MeCountDivider() {
    Spacer(
        modifier = Modifier
            .height(SpaceLarge)
            .width(SpaceExtraSmall)
            .background(LocalDividerColor.current),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// 我的订单卡片（使用真实 drawable 图标）
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun MeOrderInfo(
    orderButtons: List<OrderButtonItem>,
    toOrder: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = SpaceLarge),
            ) {
                Text(
                    text = "我的订单",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = SpaceLarge)
                        .clickable { toOrder(0) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "全部订单",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.outline,
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SpaceExtraSmall)
                    .background(LocalDividerColor.current),
            )
            Row {
                orderButtons.forEachIndexed { index, item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                // 订单页 Tab：0全部 / 1待付款 / 2待收货 / 3待评价
                                // 「退款/售后」暂无独立 Tab，先进入全部订单，避免 style=4 越界崩溃
                                val style = when (index) {
                                    0 -> 1 // 待付款
                                    1 -> 2 // 待收货
                                    2 -> 3 // 待评价
                                    else -> 0 // 退款/售后 → 全部
                                }
                                toOrder(style)
                            }
                            .padding(vertical = SpaceLarge),
                    ) {
                        // 使用真实图标资源
                        Image(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(44.dp),
                        )
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 更多服务按钮网格（接入网络图片）
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun MeServiceButtons(
    buttons: List<ButtonControl>,
    modifier: Modifier = Modifier,
    onItemClick: (ButtonControl) -> Unit,
) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(bottom = SpaceLarge)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = SpaceLarge),
            ) {
                Text(
                    text = "更多服务",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = SpaceLarge),
                ) {
                    Text(
                        text = "查看更多",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.outline,
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SpaceExtraSmall)
                    .background(LocalDividerColor.current),
            )
            // 每行 4 个，动态分行
            buttons.chunked(4).forEach { row ->
                Row {
                    row.forEach { btn ->
                        MeServiceButtonItem(
                            btn = btn,
                            modifier = Modifier.weight(1f),
                            onClick = { onItemClick(btn) },
                        )
                    }
                    // 补空位
                    repeat(4 - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun MeServiceButtonItem(
    btn: ButtonControl,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
    ) {
        // 加载网络图标（与 Compose 版一致，用 coil3 + ResourceUtil 拼接路径）
        AsyncImage(
            model = ResourceUtil.r(btn.icon),
            contentDescription = null,
            modifier = Modifier.size(50.dp),
        )
        Text(
            text = btn.title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}
