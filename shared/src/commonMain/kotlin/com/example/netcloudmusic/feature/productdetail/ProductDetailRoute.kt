package com.example.netcloudmusic.feature.productdetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.netcloudmusic.core.design.theme.largeEndRoundedCornerShape
import com.example.netcloudmusic.core.design.theme.largeStartRoundedCornerShape
import com.example.netcloudmusic.core.design.theme.md_theme_light_primary30
import com.example.netcloudmusic.core.design.theme.md_theme_transparent_88
import com.example.netcloudmusic.core.design.theme.LocalDividerColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.design.component.MyErrorView
import com.example.netcloudmusic.core.design.component.MyLoading
import com.example.netcloudmusic.core.exception.tipIcon
import com.example.netcloudmusic.core.exception.tipMessage
import com.example.netcloudmusic.util.ResourceUtil
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.tab_mall_cart
import netcloudmusic.shared.generated.resources.placeholder
import netcloudmusic.shared.generated.resources.price
import netcloudmusic.shared.generated.resources.cart
import netcloudmusic.shared.generated.resources.add_cart
import netcloudmusic.shared.generated.resources.buy_now
import netcloudmusic.shared.generated.resources.customer_service
import netcloudmusic.shared.generated.resources.number
import netcloudmusic.shared.generated.resources.product_item_info
import netcloudmusic.shared.generated.resources.product_detail_section
import netcloudmusic.shared.generated.resources.stock_selected
import netcloudmusic.shared.generated.resources.selected
import netcloudmusic.shared.generated.resources.sent_to
import netcloudmusic.shared.generated.resources.store
import netcloudmusic.shared.generated.resources.demo_address
import netcloudmusic.shared.generated.resources.demo_store
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProductDetailRoute(
    productId: String,
    toBack: () -> Unit,
    toConfirmOrder: (ProductDetailOrderRequest) -> Unit = {},
    onAddCartClick: (ProductDetailOrderRequest) -> Unit = {},
    onCartClick: () -> Unit = {},
    toChat: (String) -> Unit = {},
    onShareTextClick: () -> Unit = {},
    onShareImageClick: () -> Unit = {},
) {
    val vm: ProductDetailViewModel = viewModel(
        key = "product-detail-$productId",
        factory = viewModelFactory { initializer { ProductDetailViewModel(productId) } },
    )
    val state by vm.uiState.collectAsState()
    val spec by vm.specUiState.collectAsState()
    val showSpec by vm.isShowSpecDialog.collectAsState()
    ProductDetailScreen(
        state = state,
        spec = spec,
        showSpecDialog = showSpec,
        toBack = toBack,
        onRetry = vm::reloadData,
        onShowSpec = vm::showSpecDialog,
        onDismissSpec = vm::dismissSpecDialog,
        onSelectSpec = vm::onSubSpecSelected,
        onDecrease = vm::decrementCount,
        onIncrease = vm::incrementCount,
        onCartClick = onCartClick,
        toChat = { (state as? ProductDetailUiState.Success)?.product?.id?.let(toChat) },
        onShare = onShareImageClick,
        onShareText = onShareTextClick,
        onAddCart = {
            vm.dismissSpecDialog()
            onAddCartClick(ProductDetailOrderRequest(spec.productId, spec.selectedStockId, spec.count))
        },
        onBuy = {
            vm.dismissSpecDialog()
            toConfirmOrder(ProductDetailOrderRequest(spec.productId, spec.selectedStockId, spec.count))
        },
    )
}

@Composable
private fun ProductDetailScreen(
    state: ProductDetailUiState,
    spec: ProductSpecUiState,
    showSpecDialog: Boolean,
    toBack: () -> Unit,
    onRetry: () -> Unit,
    onShowSpec: () -> Unit,
    onDismissSpec: () -> Unit,
    onSelectSpec: (String, String) -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onCartClick: () -> Unit,
    toChat: () -> Unit,
    onShare: () -> Unit,
    onShareText: () -> Unit,
    onAddCart: () -> Unit,
    onBuy: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        when (state) {
            ProductDetailUiState.Loading -> MyLoading(Modifier.fillMaxSize())
            is ProductDetailUiState.Error -> MyErrorView(
                message = state.data.tipMessage(),
                icon = state.data.tipIcon(),
                onRetryClick = onRetry,
                modifier = Modifier.fillMaxSize(),
            )
            is ProductDetailUiState.Success -> MyContentView(
                product = state.product,
                spec = spec,
                onBack = toBack,
                onShowSpec = onShowSpec,
                onAddCart = if (state.product.specs.isNullOrEmpty()) onAddCart else onShowSpec,
                onBuy = if (state.product.specs.isNullOrEmpty()) onBuy else onShowSpec,
                onCartClick = onCartClick,
                onChat = toChat,
                onShare = onShare,
                onShareText = onShareText,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
    if (showSpecDialog) {
        ProductSpecDialog(spec, onDismissSpec, onSelectSpec, onDecrease, onIncrease, onAddCart, onBuy)
    }
}

@Composable
private fun MyContentView(
    product: Product,
    spec: ProductSpecUiState,
    onBack: () -> Unit,
    onShowSpec: () -> Unit,
    onAddCart: () -> Unit,
    onBuy: () -> Unit,
    onCartClick: () -> Unit,
    onChat: () -> Unit,
    onShare: () -> Unit,
    onShareText: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var topBarBackgroundColorAlpha by remember { mutableIntStateOf(0) }
    Box(modifier) {
        ContentView(
            product = product,
            spec = spec,
            onTopBarBackgroundColorAlpha = { topBarBackgroundColorAlpha = it },
            onShowSpec = onShowSpec,
            onAddCart = onAddCart,
            onBuy = onBuy,
            onCartClick = onCartClick,
            onChat = onChat,
        )
        ProductDetailTopBar(
            onBack = onBack,
            backgroundAlpha = topBarBackgroundColorAlpha,
            onShare = onShare,
            onShareText = onShareText,
        )
    }
}

@Composable
private fun ContentView(
    product: Product,
    spec: ProductSpecUiState,
    onTopBarBackgroundColorAlpha: (Int) -> Unit,
    onShowSpec: () -> Unit,
    onAddCart: () -> Unit,
    onBuy: () -> Unit,
    onCartClick: () -> Unit,
    onChat: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        ContentScrollView(
            product = product,
            spec = spec,
            onShowSpec = onShowSpec,
            onTopBarBackgroundColorAlpha = onTopBarBackgroundColorAlpha,
            modifier = Modifier.weight(1f),
        )
        MyProductDetailBottomBar(
            toConfirmOrder = onBuy,
            onAddCartClick = onAddCart,
            left = {
                ProductDetailIconText(painterResource(Res.drawable.tab_mall_cart), stringResource(Res.string.customer_service), onClick = onChat)
                Spacer(Modifier.size(16.dp))
                ProductDetailIconText(painterResource(Res.drawable.tab_mall_cart), stringResource(Res.string.cart), onClick = onCartClick)
                Spacer(Modifier.size(10.dp))
            },
        )
    }
}

@Composable
private fun MyProductDetailBottomBar(
    toConfirmOrder: () -> Unit = {},
    onAddCartClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    left: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 5.dp).navigationBarsPadding(),
    ) {
        left?.invoke(this)
        ProductDetailActionButton(
            text = stringResource(Res.string.add_cart), onClick = onAddCartClick,
            shape = largeStartRoundedCornerShape, modifier = Modifier.weight(1f),
            colors = listOf(MaterialTheme.colorScheme.primary, md_theme_light_primary30),
        )
        ProductDetailActionButton(
            text = stringResource(Res.string.buy_now), onClick = toConfirmOrder,
            shape = largeEndRoundedCornerShape, modifier = Modifier.weight(1f),
            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContentScrollView(product: Product, spec: ProductSpecUiState, onShowSpec: () -> Unit, onTopBarBackgroundColorAlpha: (Int) -> Unit, modifier: Modifier = Modifier) {
    val images = (listOfNotNull(product.icon) + product.icons).filter(String::isNotBlank).distinct()
    val pager = rememberPagerState(pageCount = { images.size.coerceAtLeast(1) })
    val scrollState = rememberScrollState()
    Column(modifier.fillMaxWidth().verticalScroll(scrollState)) {
            Box(Modifier.fillMaxWidth().aspectRatio(1f)) {
                HorizontalPager(state = pager, modifier = Modifier.fillMaxSize()) { page ->
                    if (images.isNotEmpty()) {
                        AsyncImage(
                            model = ResourceUtil.r(images[page]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(Res.drawable.placeholder),
                            error = painterResource(Res.drawable.placeholder),
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                if (images.size > 1) Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                ) {
                    repeat(images.size) { index ->
                        Box(Modifier.size(8.dp).clip(CircleShape).background(
                            if (pager.currentPage == index) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant
                        ))
                    }
                }
            }
            ProductDetailInfo(product, spec)
            Spacer(Modifier.height(7.dp))
            spec.selectedStockTitle?.let { selected ->
                MyProductSettingItem(stringResource(Res.string.selected), selected, Modifier.clickable(onClick = onShowSpec).padding(top = 0.7.dp))
            }
            MyProductSettingItem(stringResource(Res.string.sent_to), stringResource(Res.string.demo_address), Modifier.padding(top = 0.7.dp))
            MyProductSettingItem(stringResource(Res.string.store), stringResource(Res.string.demo_store), Modifier.padding(top = 0.7.dp))
            if (!product.detail.isNullOrBlank()) {
                Text(stringResource(Res.string.product_detail_section), style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp))
                ProductDetailHtml(product)
            }
        }
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }.collect { onTopBarBackgroundColorAlpha(it.coerceAtMost(255)) }
    }
}

@Composable
private fun ProductDetailTopBar(
    onBack: () -> Unit,
    backgroundAlpha: Int,
    onShare: () -> Unit,
    onShareText: () -> Unit,
) {
    val alpha = (backgroundAlpha / 255f).coerceIn(0f, 1f)
    Row(
        modifier = Modifier.fillMaxWidth().background(
            MaterialTheme.colorScheme.surface.copy(alpha = alpha)
        ).statusBarsPadding().padding(horizontal = 16.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProductDetailTopIcon(Icons.Default.ArrowBackIosNew, onBack)
        Box(Modifier.weight(1f))
        ProductDetailTopIcon(Icons.Default.Share, onShare)
        Spacer(Modifier.size(16.dp))
        ProductDetailTopIcon(Icons.Default.MoreVert, onShareText)
    }
}

@Composable
private fun ProductDetailIconText(painter: Painter, title: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Icon(painter = painter, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(24.dp))
        Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun ProductDetailActionButton(
    text: String,
    onClick: () -> Unit,
    shape: androidx.compose.ui.graphics.Shape,
    modifier: Modifier,
    colors: List<androidx.compose.ui.graphics.Color>,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.height(40.dp).clip(shape)
            .background(Brush.horizontalGradient(colors)).clickable(onClick = onClick),
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
private fun SpecActionButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.height(44.dp).clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary).clickable(onClick = onClick),
    ) {
        Text(text, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ProductDetailTopIcon(icon: ImageVector, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(38.dp).clip(CircleShape).background(md_theme_transparent_88)
            .clickable(onClick = onClick),
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(22.dp), tint = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun MyProductSettingItem(title: String, value: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.widthIn(min = 60.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun ProductDetailInfo(product: Product, spec: ProductSpecUiState) {
    Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(Res.string.price, spec.priceText), style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.size(5.dp))
            Text(stringResource(Res.string.price, spec.originPriceText), style = MaterialTheme.typography.labelLarge.copy(textDecoration = TextDecoration.LineThrough), color = MaterialTheme.colorScheme.outline)
        }
        Text(product.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        product.highlight?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 10.dp)) }
        Text(stringResource(Res.string.product_item_info, product.commentsCount.toString(), product.commentsRate.toString()), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline, modifier = Modifier.padding(top = 10.dp))
    }
}

@Composable
private fun ProductDetailHtml(product: Product) {
    val html = product.detail.orEmpty()
    val tokenRegex = Regex("(?is)<img[^>]*src\\s*=\\s*[\"']([^\"']+)[\"'][^>]*>|([^<]+)|(<br\\s*/?>|</p\\s*>|</div\\s*>)")
    Column(Modifier.fillMaxWidth()) {
        tokenRegex.findAll(html).forEach { match ->
            val imageUrl = match.groups[1]?.value
            if (imageUrl != null) {
                AsyncImage(
                    model = ResourceUtil.r(imageUrl.replace("&amp;", "&")),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    placeholder = painterResource(Res.drawable.placeholder),
                    error = painterResource(Res.drawable.placeholder),
                    modifier = Modifier.fillMaxWidth(),
                )
            } else if (match.groups[3] != null) {
                Spacer(Modifier.height(6.dp))
            } else {
                val text = match.groups[2]?.value.orEmpty().replace("&nbsp;", " ")
                    .replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").trim()
                if (text.isNotEmpty()) Text(text, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ProductSpecDialog(
    state: ProductSpecUiState,
    onDismiss: () -> Unit,
    onSelect: (String, String) -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onAddCart: () -> Unit,
    onBuy: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        dragHandle = {},
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) {
                AsyncImage(
                    model = state.icon?.takeIf(String::isNotBlank)?.let(ResourceUtil::r),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.placeholder),
                    modifier = Modifier.size(100.dp).clip(MaterialTheme.shapes.small),
                )
                Column(Modifier.padding(start = 16.dp).weight(1f)) {
                    Text(stringResource(Res.string.price, state.priceText),
                        style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                    Text(stringResource(Res.string.stock_selected, state.selectedStockTitle.orEmpty()),
                        style = MaterialTheme.typography.bodyMedium)
                }
            }
            Column(Modifier.fillMaxWidth().height(350.dp).padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())) {
                state.specs.forEach { spec ->
                    Text(spec.title, style = MaterialTheme.typography.titleSmall)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)) {
                        spec.data.orEmpty().forEach { option ->
                            Text(option.title, color = if (option.id == spec.selectedSubId) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.clip(CircleShape).background(
                                    if (option.id == spec.selectedSubId) MaterialTheme.colorScheme.primary else LocalDividerColor.current)
                                    .clickable { onSelect(spec.id, option.id) }
                                    .padding(horizontal = 16.dp, vertical = 10.dp))
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(Res.string.number), modifier = Modifier.weight(1f))
                    IconButton(onClick = onDecrease) { Icon(Icons.Default.Remove, contentDescription = null) }
                    Text(state.count.toString())
                    IconButton(onClick = onIncrease) { Icon(Icons.Default.Add, contentDescription = null) }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) {
                SpecActionButton(stringResource(Res.string.add_cart), onAddCart, Modifier.weight(1f))
                SpecActionButton(stringResource(Res.string.buy_now), onBuy, Modifier.weight(1f))
            }
        }
    }
}
