package com.example.netcloudmusic.feature.productdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.component.MyNumberInput
import com.example.netcloudmusic.core.design.theme.LocalDividerColor
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceMediumHeight
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.design.theme.SpacerOuterWidth
import com.example.netcloudmusic.core.model.Spec
import com.example.netcloudmusic.core.util.ResourceUtil
import com.example.netcloudmusic.feature.productdetail.ProductSpecUiState
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.number
import netcloudmusic.shared.generated.resources.price
import netcloudmusic.shared.generated.resources.stock_selected
import org.jetbrains.compose.resources.stringResource

/**
 * 商品规格选择弹窗（与 ClientApp 完整对齐）
 *
 * 使用 MyNumberInput 组件替代手写 +/- 按钮，
 * 底部操作栏复用 MyProductDetailBottomBar。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProductSpecDialog(
    data: ProductSpecUiState = ProductSpecUiState(),
    toConfirmOrder: () -> Unit = {},
    onAddCartClick: () -> Unit = {},
    onSubSpecSelected: (String, String, String) -> Unit = { _, _, _ -> },
    onDecrementCount: () -> Unit = {},
    onIncrementCount: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        dragHandle = {},  // 去掉顶部拖拽控件，与 ClientApp 一致
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── 商品简要信息 ──────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpaceOuter),
            ) {
                AsyncImage(
                    model = data.icon.takeIf(String::isNotBlank)?.let(ResourceUtil::r),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(LocalDividerColor.current),
                )
                SpacerOuterWidth()
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.price, data.priceFloat),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    SpaceMediumHeight()
                    Text(
                        text = stringResource(
                            Res.string.stock_selected,
                            data.selectedStockTitle.orEmpty(),
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            // ── 规格列表 + 数量选择（可滚动区域）────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(horizontal = SpaceOuter)
                    .verticalScroll(rememberScrollState()),
            ) {
                data.specs?.forEach { spec ->
                    ProductSpecItem(spec, onSubSpecSelected = onSubSpecSelected)
                }

                // 数量选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = SpaceOuter),
                ) {
                    Text(
                        text = stringResource(Res.string.number),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                    MyNumberInput(
                        count = data.count,
                        onDecrementCount = onDecrementCount,
                        onIncrementCount = onIncrementCount,
                    )
                }
            }

            // ── 底部操作栏（加入购物车 / 立即购买）──────────
            MyProductDetailBottomBar(
                toConfirmOrder = toConfirmOrder,
                onAddCartClick = onAddCartClick,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductSpecItem(
    data: Spec,
    modifier: Modifier = Modifier,
    onSubSpecSelected: (String, String, String) -> Unit = { _, _, _ -> },
) {
    Text(
        text = data.title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(SpaceMedium),
        verticalArrangement = Arrangement.spacedBy(SpaceMedium),
        modifier = Modifier.padding(
            start = SpaceOuter,
            end = SpaceOuter,
            top = SpaceMedium,
            bottom = SpaceOuter,
        ),
    ) {
        data.data?.forEach { subItem ->
            Box(
                modifier = modifier
                    .clip(CircleShape)
                    .background(
                        if (data.selectedSubId == subItem.id)
                            MaterialTheme.colorScheme.primary
                        else
                            LocalDividerColor.current,
                    )
                    .clickable {
                        onSubSpecSelected(data.id, subItem.id, subItem.title)
                    },
            ) {
                Text(
                    text = subItem.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (data.selectedSubId == subItem.id)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(
                        horizontal = SpaceOuter,
                        vertical = SpaceExtraMedium,
                    ),
                )
            }
        }
    }
}
