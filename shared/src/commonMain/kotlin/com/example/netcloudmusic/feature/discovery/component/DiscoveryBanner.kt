package com.example.netcloudmusic.feature.discovery.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.component.HorizontalPagerIndicator
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.model.Ad
import com.example.netcloudmusic.core.util.ResourceUtil
import kotlinx.coroutines.delay

/**
 * 首页 Banner 轮播图
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiscoveryBanner(
    data: List<Ad>,
    onAdClick: (Ad) -> Unit = {},
) {
    if (data.isEmpty()) return
    
    Card {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2.4f)
                .clip(MaterialTheme.shapes.extraSmall),
        ) {
            val pagerState = rememberPagerState(pageCount = { data.size })

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                val item = data[page]
                AsyncImage(
                    model = ResourceUtil.r(item.icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAdClick(item) },
                )
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                pageCount = data.size,
                modifier = Modifier.padding(SpaceMedium),
            )

            // 轮播页进入组合后每 3 秒自动切换一次。LaunchedEffect 会在页面离开
            // 或广告数量变化时自动取消旧协程，避免重复启动多个轮播任务。
            if (data.size > 1) {
                LaunchedEffect(pagerState, data.size) {
                    while (true) {
                        delay(3_000L)
                        val nextPage = (pagerState.currentPage + 1) % data.size
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            }
        }
    }
}
