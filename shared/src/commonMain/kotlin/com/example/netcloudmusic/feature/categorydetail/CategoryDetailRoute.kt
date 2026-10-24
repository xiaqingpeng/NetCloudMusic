package com.example.netcloudmusic.feature.categorydetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.util.ResourceUtil
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.placeholder
import org.jetbrains.compose.resources.painterResource

/**
 * 三级子分类详情页入口
 */
@Composable
fun CategoryDetailRoute(
    arg: CategoryDetailArg,
    toBack: () -> Unit = {},
    toProductDetail: (String) -> Unit = {},
) {
    CategoryDetailScreen(
        arg = arg,
        toBack = toBack,
    )
}

@Composable
fun CategoryDetailScreen(
    arg: CategoryDetailArg,
    toBack: () -> Unit = {},
) {
    var topBarAlpha by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        CategoryDetailContentView(
            arg = arg,
            onScrollY = { scrollY ->
                topBarAlpha = scrollY.coerceIn(0, 255)
            }
        )

        // 沉浸式悬浮顶栏，随滚动渐显背景
        CategoryDetailTopBar(
            toBack = toBack,
            topBarAlpha = topBarAlpha,
        )
    }
}

// ── 内容滚动区 ────────────────────────────────────────────────────────────────

@Composable
private fun CategoryDetailContentView(
    arg: CategoryDetailArg,
    onScrollY: (Int) -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Banner 图（使用分类图标撑满 1:1 区域）
        CategoryDetailBanner(icon = arg.icon)

        // 分类名称信息卡
        CategoryDetailInfoCard(arg = arg)

        // 面包屑路径行
        CategoryDetailBreadcrumbItem(arg = arg)
    }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }.collect { onScrollY(it) }
    }
}

// ── Banner ────────────────────────────────────────────────────────────────────

@Composable
private fun CategoryDetailBanner(icon: String?) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (!icon.isNullOrEmpty()) {
            AsyncImage(
                model = ResourceUtil.r(icon),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.placeholder),
                error = painterResource(Res.drawable.placeholder),
                onError = {
                    println("Category detail image load failed: ${ResourceUtil.r(icon)}")
                },
            )
        } else {
            Image(
                painter = painterResource(Res.drawable.placeholder),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

// ── 信息卡片 ──────────────────────────────────────────────────────────────────

@Composable
private fun CategoryDetailInfoCard(arg: CategoryDetailArg) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        // 三级分类名称
        Text(
            text = arg.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )

        // id & sort 次要信息
        Text(
            text = "ID: ${arg.id}   排序: ${arg.sort ?: "-"}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

// ── 面包屑行 ──────────────────────────────────────────────────────────────────

@Composable
private fun CategoryDetailBreadcrumbItem(arg: CategoryDetailArg) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "所属分类",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.widthIn(min = 60.dp),
        )

        // 面包屑：一级 › 二级 › 三级（高亮）
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = arg.firstName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(10.dp),
            )
            Text(
                text = arg.secondName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(10.dp),
            )
            Text(
                text = arg.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

// ── 沉浸式顶栏 ────────────────────────────────────────────────────────────────

@Composable
private fun CategoryDetailTopBar(
    toBack: () -> Unit = {},
    topBarAlpha: Int = 0,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(255, 255, 255, topBarAlpha))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        IconButton(onClick = toBack) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "返回"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
