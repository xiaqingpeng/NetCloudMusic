package com.example.netcloudmusic.core.design.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.theme.Space3XLargeHeight
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.bg_error
import netcloudmusic.shared.generated.resources.click_retry
import netcloudmusic.shared.generated.resources.error_load
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MyErrorView(
    throwable: Throwable? = null,
    message: String = stringResource(Res.string.error_load),
    icon: DrawableResource = Res.drawable.bg_error,
    onRetryClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .clickable { onRetryClick() }
            .padding(SpaceOuter)
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(icon),
            contentDescription = null
        )

        Space3XLargeHeight()

        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )

        Space3XLargeHeight()

        OutlinedButton(
            onClick = onRetryClick,
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .widthIn(200.dp)
        ) {
            Text(text = stringResource(Res.string.click_retry))
        }
    }
}
