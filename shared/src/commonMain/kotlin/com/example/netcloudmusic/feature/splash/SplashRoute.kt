package com.example.netcloudmusic.feature.splash


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.util.SuperDateUtil
import org.jetbrains.compose.resources.painterResource
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.splash_banner
import netcloudmusic.shared.generated.resources.splash_logo
import netcloudmusic.shared.generated.resources.skip_seconds
import netcloudmusic.shared.generated.resources.copyright
import netcloudmusic.shared.generated.resources.splash_banner_description
import netcloudmusic.shared.generated.resources.splash_logo_description
import org.jetbrains.compose.resources.stringResource

/**
 * 启动界面
 */
@Composable
fun SplashRoute(
    toGuide: () -> Unit,
    toMain: () -> Unit
) {
    // 另外为 viewModel() 提供了显式 factory，避免 iOS 使用默认反射工厂时出现 Factory.create(...) is not implemented 崩溃。
    // not implemented on iOS/Native.
    val viewModel: SplashViewModel = viewModel(
        factory = viewModelFactory {
            initializer { SplashViewModel() }
        }
    )
    val timeLeft by viewModel.timeLeft.collectAsState()
    val navigateToMain by viewModel.navigateToMain.collectAsState()

    LaunchedEffect(navigateToMain) {
        if (navigateToMain) toMain()
    }

    SplashScreen(
        toGuide = toGuide,
        timeLeft = timeLeft,
        onSkipClick = viewModel::onSkipClick,
        toMain = toMain
    )
}


@Composable
fun SplashScreen(
    toGuide: () -> Unit = {},
    timeLeft: Long = 3L,
    onSkipClick: () -> Unit = {},
    toMain: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        // 启动界面 Banner
        Image(
            painter = painterResource(Res.drawable.splash_banner),
            contentDescription = stringResource(Res.string.splash_banner_description),
            modifier = Modifier
                .padding(top = 150.dp)
                .align(Alignment.TopCenter)
        )

        // 启动 Logo
        Image(
            painter = painterResource(Res.drawable.splash_logo),
            contentDescription = stringResource(Res.string.splash_logo_description),
            modifier = Modifier
                .padding(bottom = 60.dp)
                .align(Alignment.BottomCenter)
        )

        // 版权文本
        Text(
            text = stringResource(
                Res.string.copyright,
                SuperDateUtil.currentYear()
            ),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter)
                .clickable {
                    toMain()
                }
        )

        Button(
            onClick = onSkipClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 16.dp)
        ) {
            Text(stringResource(Res.string.skip_seconds, timeLeft))
        }
    }
}
