package com.example.netcloudmusic.feature.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.model.User
import com.example.netcloudmusic.core.util.ResourceUtil

/**
 * 主界面抽屉内容。
 */
@Composable
fun MainDrawerContent(
    toLogin: () -> Unit,
    toSettings: () -> Unit,
    toReceivingAddress: () -> Unit,
    toAbout: () -> Unit,
    toProfile: () -> Unit,
    toGithubProfile: () -> Unit = {},
    toOrder: (Int) -> Unit,
    user: User?,
    isLogin: Boolean,
    isGithubLogin: Boolean = false,
    closeDrawer: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(32.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = isLogin) {
                    closeDrawer()
                    if (isGithubLogin) toGithubProfile() else toProfile()
                },
        ) {
            AsyncImage(
                model = ResourceUtil.r(user?.icon.orEmpty()),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            )
            Text(
                text = if (isLogin) user?.nickname ?: "用户" else "登录或注册",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            )
            if (!isLogin) {
                TextButton(onClick = { closeDrawer(); toLogin() }) { Text("登录") }
            }
        }
        Spacer(Modifier.height(16.dp))
        DrawerVipHint()
        Spacer(Modifier.height(16.dp))
        DrawerSectionTitle("我的服务")
        if (isGithubLogin) {
            DrawerItem("GitHub 主页", Icons.Default.Person, closeDrawer) { toGithubProfile() }
        } else {
            DrawerItem("个人资料", Icons.Default.Person, closeDrawer) { toProfile() }
        }
        Spacer(Modifier.height(8.dp))
        DrawerItem("我的订单", Icons.Default.ShoppingBag, closeDrawer) { toOrder(0) }
        Spacer(Modifier.height(8.dp))
        DrawerItem("收货地址", Icons.Default.Place, closeDrawer) { toReceivingAddress() }

        DrawerSectionTitle("更多")
        DrawerItem("设置", Icons.Default.Settings, closeDrawer) { toSettings() }
        Spacer(Modifier.height(8.dp))
        DrawerItem("关于", Icons.Default.Info, closeDrawer) { toAbout() }

    }
}
