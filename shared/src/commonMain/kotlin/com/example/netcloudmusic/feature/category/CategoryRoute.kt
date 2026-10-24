package com.example.netcloudmusic.feature.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.design.theme.LocalDividerColor
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.design.theme.SpaceSmallHeight
import com.example.netcloudmusic.core.design.theme.extraSmallRoundedCornerShape
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.model.Category
import com.example.netcloudmusic.core.util.ResourceUtil
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.category
import netcloudmusic.shared.generated.resources.placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 分类界面
 */
@Composable
fun CategoryRoute(
    toCategoryDetail: (com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg) -> Unit = {},
    viewModel: CategoryViewModel = viewModel {
        CategoryViewModel(AppContainerProvider.instance.categoryRepository)
    }
) {
    val leftDatum by viewModel.leftDatum.collectAsStateWithLifecycle()
    val rightDatum by viewModel.rightDatum.collectAsStateWithLifecycle()
    val leftSelectedId by viewModel.leftSelectedId.collectAsStateWithLifecycle()

    CategoryScreen(
        leftDatum = leftDatum,
        leftSelectedId = leftSelectedId,
        rightDatum = rightDatum,
        loadRightData = viewModel::loadRightData,
        toCategoryDetail = toCategoryDetail,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    leftDatum: List<Category> = listOf(),
    leftSelectedId: String = "",
    rightDatum: List<Category> = listOf(),
    loadRightData: (String) -> Unit = {},
    toCategoryDetail: (com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg) -> Unit = {},
) {
    // 从左侧列表中找到当前选中项的名称，作为 firstName
    val category1Name = leftDatum.find { it.id == leftSelectedId }?.title.orEmpty()

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                title = stringResource(Res.string.category)
            )
        },
        // 排除底部导航栏边距
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LeftCategoryList(
                leftDatum = leftDatum,
                leftSelectedId = leftSelectedId,
                loadRightData = loadRightData,
                modifier = Modifier.width(110.dp)
            )
            RightCategoryList(
                rightDatum = rightDatum,
                category1Id = leftSelectedId,
                category1Name = category1Name,
                toCategoryDetail = toCategoryDetail,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun LeftCategoryList(
    leftDatum: List<Category>,
    leftSelectedId: String,
    loadRightData: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxHeight()
    ) {
        items(leftDatum) { item ->
            LeftCategoryItem(
                title = item.title,
                selected = leftSelectedId == item.id,
                modifier = Modifier.clickable {
                    loadRightData(item.id)
                }
            )
        }
    }
}

@Composable
fun LeftCategoryItem(
    title: String,
    selected: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                if (selected)
                    MaterialTheme.colorScheme.surfaceVariant
                else
                    Color.Transparent
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )

        if (selected) {
            Spacer(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun RightCategoryList(
    rightDatum: List<Category>,
    category1Id: String,
    category1Name: String,
    toCategoryDetail: (com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        items(rightDatum) { item ->
            RightCategoryItem(
                data = item,
                category1Id = category1Id,
                category1Name = category1Name,
                toCategoryDetail = toCategoryDetail
            )
        }
    }
}

@Composable
fun RightCategoryItem(
    data: Category,
    category1Id: String,
    category1Name: String,
    toCategoryDetail: (com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = data.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = SpaceOuter)
            )
        }

        // 子分类列表，每行 3 个，data.id 是二级分类 id，data.title 是二级分类名称
        data.data?.chunked(3)?.forEach { subCategoryList ->
            RightCategorySubItemRow(
                data = subCategoryList,
                category1Id = category1Id,
                category1Name = category1Name,
                category2Id = data.id,
                category2Name = data.title,
                toCategoryDetail = toCategoryDetail
            )
        }
    }
}

@Composable
fun RightCategorySubItemRow(
    data: List<Category>,
    category1Id: String,
    category1Name: String,
    category2Id: String,
    category2Name: String,
    toCategoryDetail: (com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpaceMedium)
    ) {
        data.forEach { subCategory ->
            RightCategoryItemIconTitle(
                icon = subCategory.icon,
                title = subCategory.title,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        toCategoryDetail(
                            com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg(
                                id = subCategory.id,
                                title = subCategory.title,
                                icon = subCategory.icon,
                                parentId = subCategory.parentId,
                                sort = subCategory.sort,
                                firstId = category1Id,
                                secondId = category2Id,
                                firstName = category1Name,
                                secondName = category2Name,
                            )
                        )
                    }
            )
        }
        // 补齐空位，保持每行 3 列对齐
        repeat(3 - data.size) {
            Box(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun RightCategoryItemIconTitle(
    icon: String?,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = SpaceMedium)
    ) {
        val iconModifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .aspectRatio(1f)
            .clip(extraSmallRoundedCornerShape)

        if (!icon.isNullOrEmpty()) {
            AsyncImage(
                model = ResourceUtil.r(icon),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = iconModifier
                    .background(LocalDividerColor.current),
                placeholder = painterResource(Res.drawable.placeholder),
                error = painterResource(Res.drawable.placeholder),
                onError = {
                    println("Category image load failed: ${ResourceUtil.r(icon)}")
                },
            )
        } else {
            Image(
                painter = painterResource(Res.drawable.placeholder),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = iconModifier
                    .background(LocalDividerColor.current)
            )
        }

        SpaceSmallHeight()

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = SpaceOuter)
        )
    }
}
