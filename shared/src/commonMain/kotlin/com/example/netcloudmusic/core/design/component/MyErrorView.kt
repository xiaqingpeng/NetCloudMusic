package com.example.netcloudmusic.core.design.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.netcloudmusic.core.exception.tipIcon
import com.example.netcloudmusic.core.exception.tipMessage
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
    message: String? = null,
    retryButtonText: String = stringResource(Res.string.click_retry),
    icon: DrawableResource? = null,
    onRetryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(16.dp),
    ) {
        Image(
            painter = painterResource(icon ?: throwable?.tipIcon() ?: Res.drawable.bg_error),
            contentDescription = null,
            modifier = Modifier.size(300.dp),
        )
        Text(
            text = message ?: throwable?.tipMessage() ?: stringResource(Res.string.error_load),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
        )
        OutlinedButton(
            onClick = onRetryClick,
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
            contentPadding = PaddingValues(horizontal = 24.dp),
            modifier = Modifier.padding(top = 30.dp).widthIn(min = 200.dp),
        ) {
            Text(retryButtonText)
        }
    }
}
