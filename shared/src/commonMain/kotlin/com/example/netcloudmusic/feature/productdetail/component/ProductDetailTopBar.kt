package com.example.netcloudmusic.feature.productdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.design.theme.SpaceSmall
import com.example.netcloudmusic.core.design.theme.SpacerOuterWidth
import com.example.netcloudmusic.core.design.theme.md_theme_transparent_88

/**
 * 商品详情顶部导航栏
 *
 * 随页面滚动通过 [topBarBackgroundColorAlpha]（0~255）渐变背景。
 */
@Composable
fun MyProductDetailTopBar(
    toBack: () -> Unit = {},
    onShareImageClick: () -> Unit = {},
    onShareTextClick: () -> Unit = {},
    topBarBackgroundColorAlpha: Int = 0,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(255, 255, 255, topBarBackgroundColorAlpha)
            )
            .statusBarsPadding()
            .padding(horizontal = SpaceOuter, vertical = SpaceSmall),
    ) {
        CircleImageButton(icon = Icons.AutoMirrored.Filled.ArrowBackIos, onClick = toBack)
        Spacer(modifier = Modifier.weight(1f))
        CircleImageButton(icon = Icons.Default.Share, onClick = onShareImageClick)
        SpacerOuterWidth()
        CircleImageButton(icon = Icons.Default.MoreVert, onClick = onShareTextClick)
    }
}

/** 圆形半透明图标按钮，用于 TopBar 的返回、分享等操作 */
@Composable
fun CircleImageButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(38.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .background(md_theme_transparent_88),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp),
        )
    }
}
