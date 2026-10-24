package com.example.netcloudmusic.feature.discovery.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.model.CropData
import com.example.netcloudmusic.core.model.CropRectArea
import com.example.netcloudmusic.core.util.ResourceUtil

// ─────────────────────────────────────────────
// 裁剪区域大图（可点击热区）
// ─────────────────────────────────────────────

/**
 * 将后端的比例坐标（0.0~1.0）转换为屏幕像素坐标。
 * 后端坐标系：left/top/right/bottom 均为相对图片宽高的比例值。
 * 转换后才能与 pointerInput 里 offset（像素）做比较。
 */
fun convertToPixelAreas(
    data: CropData,
    itemWidth: Int,
    itemHeight: Int,
): List<CropRectArea> = data.data.map { r ->
    CropRectArea(
        left   = itemWidth  * r.left,
        top    = itemHeight * r.top,
        right  = itemWidth  * r.right,
        bottom = itemHeight * r.bottom,
        uri    = r.uri,
    )
}

/**
 * 判断点击位置是否在热区，没有命中则返回 null。
 */
fun findClickedArea(areas: List<CropRectArea>, x: Float, y: Float): CropRectArea? =
    areas.find { x in it.left..it.right && y in it.top..it.bottom }

@Composable
fun CropButtonColumn(
    data: List<CropData>,
    toUri: (String) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(SpaceExtraMedium),
        modifier = Modifier.fillMaxWidth(),
    ) {
        data.forEach { cropData ->
            CropButtonView(
                data = cropData,
                onAreaClicked = { area -> area.uri?.let { toUri(it) } },
            )
        }
    }
}

@Composable
fun CropButtonView(
    data: CropData,
    onAreaClicked: (CropRectArea) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    // onGloballyPositioned 测量到的实际像素尺寸，初始为 0 等待首次布局回调
    var itemWidth  by remember { mutableIntStateOf(0) }
    var itemHeight by remember { mutableIntStateOf(0) }

    // itemWidth / itemHeight 变化时自动重算像素热区，其余重组直接用缓存值
    val areas by remember {
        derivedStateOf { convertToPixelAreas(data, itemWidth, itemHeight) }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(data.width * 1.0f / data.height.coerceAtLeast(1))
            // 布局完成后拿到实际像素尺寸，写入后 derivedStateOf 自动触发热区重算
            .onGloballyPositioned { coords ->
                itemWidth  = coords.size.width
                itemHeight = (itemWidth * 1.0f / data.width * data.height).toInt()
                println("CropButtonView width=$itemWidth")
            },
    ) {
        AsyncImage(
            model = ResourceUtil.r(data.icon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                // key 传 areas：热区像素坐标更新时重启手势检测，确保命中判断用最新坐标
                .pointerInput(areas) {
                    detectTapGestures { offset ->
                        findClickedArea(areas, offset.x, offset.y)?.let(onAreaClicked)
                    }
                },
        )
    }
}
