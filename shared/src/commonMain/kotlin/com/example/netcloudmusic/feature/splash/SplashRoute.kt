package com.example.netcloudmusic.feature.splash


import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.util.SuperDateUtil
import kotlinx.coroutines.delay
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
    var timeLeft by remember { mutableStateOf(3L) }
    var hasNavigated by remember { mutableStateOf(false) }

    fun navigateToMainOnce() {
        if (!hasNavigated) {
            hasNavigated = true
            toMain()
        }
    }

    LaunchedEffect(Unit) {
        repeat(3) { second ->
            timeLeft = 3L - second
            delay(1000)
        }
        navigateToMainOnce()
    }

    SplashScreen(
        toGuide = toGuide,
        timeLeft = timeLeft,
        onSkipClick = ::navigateToMainOnce,
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
        modifier = Modifier.fillMaxSize(),
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
