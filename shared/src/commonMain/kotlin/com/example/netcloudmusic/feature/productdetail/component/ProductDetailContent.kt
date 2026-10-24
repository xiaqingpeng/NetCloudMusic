package com.example.netcloudmusic.feature.productdetail.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.component.HorizontalPagerIndicator
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.design.theme.SpaceExtraSmall
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceMediumWidth
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.design.theme.SpacerOuterWidth
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.util.ResourceUtil
import com.example.netcloudmusic.feature.productdetail.ProductSpecUiState
import kotlinx.coroutines.flow.collectLatest
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.cart
import netcloudmusic.shared.generated.resources.customer_service
import netcloudmusic.shared.generated.resources.sent_to
import netcloudmusic.shared.generated.resources.store
import org.jetbrains.compose.resources.stringResource

/**
 * 商品详情主内容视图
 *
 * 由 [MyContentView] 组合 [ContentView]（内容主体）和 [MyProductDetailTopBar]（叠加在上方）。
 */
@Composable
fun MyContentView(
    data: Product,
    specUiState: ProductSpecUiState,
    toBack: () -> Unit = {},
    toConfirmOrder: () -> Unit = {},
    onAddCartClick: () -> Unit = {},
    showSpecDialog: () -> Unit = {},
    onShareTextClick: () -> Unit = {},
    onShareImageClick: () -> Unit = {},
) {
    var topBarAlpha by remember { mutableIntStateOf(0) }

    ContentView(
        data = data,
        specUiState = specUiState,
        onTopBarBackgroundColorAlpha = { topBarAlpha = it },
        toConfirmOrder = toConfirmOrder,
        onAddCartClick = onAddCartClick,
        showSpecDialog = showSpecDialog,
    )
    // TopBar 叠加在内容上方，alpha 随滚动渐变
    MyProductDetailTopBar(
        toBack = toBack,
        topBarBackgroundColorAlpha = topBarAlpha,
        onShareImageClick = onShareImageClick,
        onShareTextClick = onShareTextClick,
    )
}

/** 内容主体：可滚动区域 + 底部操作栏 */
@Composable
fun ContentView(
    data: Product,
    specUiState: ProductSpecUiState,
    onTopBarBackgroundColorAlpha: (Int) -> Unit = {},
    toConfirmOrder: () -> Unit = {},
    onAddCartClick: () -> Unit = {},
    showSpecDialog: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ContentScrollView(
            data = data,
            specUiState = specUiState,
            onTopBarBackgroundColorAlpha = onTopBarBackgroundColorAlpha,
            showSpecDialog = showSpecDialog,
            modifier = Modifier.weight(1f),
        )
        MyProductDetailBottomBar(
            toConfirmOrder = toConfirmOrder,
            onAddCartClick = onAddCartClick,
            left = {
                ProductDetailIconText(
                    icon = Icons.Default.ShoppingCart,
                    title = stringResource(Res.string.customer_service)
                )
                SpacerOuterWidth()
                ProductDetailIconText(
                    icon = Icons.Default.ShoppingCart,
                    title = stringResource(Res.string.cart)
                )
                SpaceMediumWidth()
            },
        )
    }
}

@Composable
fun ContentScrollView(
    data: Product,
    specUiState: ProductSpecUiState,
    modifier: Modifier = Modifier,
    onTopBarBackgroundColorAlpha: (Int) -> Unit = {},
    showSpecDialog: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        ProductDetailBanner(data.icons)
        ProductDetailInfo(data, specUiState)
        Spacer(modifier = Modifier.height(SpaceExtraMedium))

        specUiState.selectedStockTitle?.let {
            MyProductSettingItem(
                title = "已选",
                value = it,
                modifier = Modifier
                    .clickable { showSpecDialog() }
                    .padding(top = SpaceExtraSmall),
            )
        }
        MyProductSettingItem(
            title = stringResource(Res.string.sent_to),
            value = "四川省 成都市 高新区",
            modifier = Modifier.padding(top = SpaceExtraSmall),
        )
        MyProductSettingItem(
            title = stringResource(Res.string.store),
            value = "小米之家 四川省成都市印象城专卖店",
            modifier = Modifier.padding(top = SpaceExtraSmall),
        )
        
        // 显示商品详情（暂时使用 Text 显示 HTML）
        data.detail?.let { detailHtml ->
            Text(
                text = detailHtml,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SpaceExtraMedium, start = SpaceOuter, end = SpaceOuter, bottom = SpaceOuter),
            )
        }
    }

    // snapshotFlow 把 Compose State（scrollState.value）桥接成 Flow，
    // collectLatest 确保始终处理最新的滚动值，钳制 alpha 上限为 255。
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }.collectLatest { scrollY ->
            onTopBarBackgroundColorAlpha(scrollY.coerceAtMost(255))
        }
    }
}

/** 商品图片轮播图 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailBanner(data: List<String>) {
    val pagerState = rememberPagerState { data.size }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            AsyncImage(
                model = ResourceUtil.r(data[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = data.size,
            modifier = Modifier.padding(SpaceMedium),
            activeColor = MaterialTheme.colorScheme.primary,
        )
    }
}

/** 左侧图标 + 文字的竖排按钮，用于底部操作栏左侧插槽 */
@Composable
fun ProductDetailIconText(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
