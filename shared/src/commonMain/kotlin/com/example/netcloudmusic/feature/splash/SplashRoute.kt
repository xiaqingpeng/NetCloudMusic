package com.example.netcloudmusic.feature.splash


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import org.jetbrains.compose.resources.painterResource
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.splash_banner
import netcloudmusic.shared.generated.resources.splash_logo
import kotlinx.coroutines.delay

/**
 * 启动界面
 */
@Composable
fun SplashRoute(
    toGuide: () -> Unit
) {
    var timeLeft by remember { mutableStateOf(3L) }

    LaunchedEffect(Unit) {
        repeat(3) { second ->
            timeLeft = 3L - second
            delay(1000)
        }
        toGuide()
    }

    SplashScreen(
        toGuide = toGuide,
        timeLeft = timeLeft,
        toNext = toGuide
    )
}


@Composable
fun SplashScreen(
    toGuide: () -> Unit = {},
    timeLeft: Long = 3L,
    toNext: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 启动界面 Banner
        Image(
            painter = painterResource(Res.drawable.splash_banner),
            contentDescription = "看见更大的世界",
            modifier = Modifier
                .padding(top = 150.dp)
                .align(Alignment.TopCenter)
        )

        // 启动 Logo
        Image(
            painter = painterResource(Res.drawable.splash_logo),
            contentDescription = "头条今日头条",
            modifier = Modifier
                .padding(bottom = 60.dp)
                .align(Alignment.BottomCenter)
        )

        // 版权文本
        Text(
            text = "© ${SuperDateUtil.currentYear()} 今日头条",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter)
                .clickable {
                    toGuide()
                }
        )

        Button(
            onClick = toNext,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 16.dp)
        ) {
            Text("跳过${timeLeft}s")
        }
    }
}
