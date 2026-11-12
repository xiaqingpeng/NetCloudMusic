package com.example.netcloudmusic.feature.guide

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.design.component.HorizontalPagerIndicator
import com.example.netcloudmusic.core.design.theme.Space3XLarge
import com.example.netcloudmusic.core.design.theme.Space3XLargeWidth
import com.example.netcloudmusic.core.design.theme.Space4XLarge
import com.example.netcloudmusic.core.di.AppContainerProvider
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.experience_now
import netcloudmusic.shared.generated.resources.guide1
import netcloudmusic.shared.generated.resources.guide2
import netcloudmusic.shared.generated.resources.guide3
import netcloudmusic.shared.generated.resources.guide4
import netcloudmusic.shared.generated.resources.guide5
import netcloudmusic.shared.generated.resources.login_or_register
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 引导界面
 */
@Composable
fun GuideRoute(
    toMain: () -> Unit,
    toLogin: () -> Unit,
) {
    val viewModel: GuideViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                GuideViewModel(
                    appPreferenceRepository = AppContainerProvider.instance.appPreferenceRepository,
                )
            }
        },
    )

    val guides = remember {
        listOf(
            Res.drawable.guide1,
            Res.drawable.guide2,
            Res.drawable.guide3,
            Res.drawable.guide4,
            Res.drawable.guide5,
        )
    }

    GuideScreen(
        datum = guides,
        toMain = toMain,
        toLogin = toLogin,
        setNotShowGuide = viewModel::setNotShowGuide,
    )
}

@Composable
fun GuideScreen(
    datum: List<DrawableResource> = emptyList(),
    toLogin: () -> Unit = {},
    toMain: () -> Unit = {},
    setNotShowGuide: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        GuideBannerView(
            datum = datum,
            modifier = Modifier.weight(1f),
        )

        GuideControlView(
            toLogin = {
                setNotShowGuide()
                toLogin()
            },
            toMain = {
                setNotShowGuide()
                toMain()
            },
        )
    }
}

@Composable
private fun GuideControlView(
    toLogin: () -> Unit = {},
    toMain: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Space3XLarge),
    ) {
        Button(
            onClick = toLogin,
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier.weight(1f),
        ) {
            Text(text = stringResource(Res.string.login_or_register))
        }

        Space3XLargeWidth()

        OutlinedButton(
            onClick = toMain,
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier.weight(1f),
        ) {
            Text(text = stringResource(Res.string.experience_now))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GuideBannerView(
    datum: List<DrawableResource>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { datum.size }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val item = datum[page]
            Image(
                painter = painterResource(item),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (datum.isNotEmpty()) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                pageCount = datum.size,
                modifier = Modifier.padding(bottom = Space4XLarge),
                activeColor = MaterialTheme.colorScheme.primary,
                inactiveColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        }
    }
}
