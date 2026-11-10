package com.example.netcloudmusic.feature.me.compoent

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.theme.SpaceExtraMediumWidth
import com.example.netcloudmusic.core.design.theme.SpaceSmallHeight
import com.example.netcloudmusic.core.design.theme.SpacerOuterWidth
import com.example.netcloudmusic.core.model.User
import com.example.netcloudmusic.core.util.ResourceUtil
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.default_avatar
import netcloudmusic.shared.generated.resources.login_or_register
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 未登录时的用户头像占位行 */
@Composable
fun DefaultUserProfile(toLogin: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { toLogin() },
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(Res.drawable.default_avatar),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape),
        )
        SpacerOuterWidth()
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.login_or_register),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            SpaceSmallHeight()
            Text(
                text = "点击登录或注册",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp),
        )
    }
}

/** 已登录时的用户头像行 */
@Composable
fun UserProfile(data: User, toProfile: () -> Unit, toCode: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { toProfile() },
    ) {
        AsyncImage(
            model = ResourceUtil.r(data.icon ?: ""),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape),
        )
        SpacerOuterWidth()
        Column(Modifier.weight(1f)) {
            Text(
                text = data.nickname ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            SpaceSmallHeight()
            Text(
                text = "点击查看主页",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
            )
        }
        Button(onClick = toCode) {
            Icon(imageVector = Icons.Default.QrCode, contentDescription = null, tint = Color.White)
        }
        SpaceExtraMediumWidth()
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp),
        )
    }
}
