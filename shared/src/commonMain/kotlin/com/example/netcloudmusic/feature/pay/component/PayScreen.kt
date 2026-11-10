package com.example.netcloudmusic.feature.pay.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.util.Constant
import com.example.netcloudmusic.feature.pay.PayUiState
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.pay_order
import org.jetbrains.compose.resources.stringResource

/**
 * 支付主屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayScreen(
    uiState: PayUiState = PayUiState.Loading,
    payChannel: Int = Constant.ALIPAY,
    payChannelChanged: (Int) -> Unit = {},
    toBack: () -> Unit = {},
    onPrimaryClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                toBack = toBack,
                title = stringResource(Res.string.pay_order),
            )
        },
        bottomBar = {
            PayBottomBar(onPrimaryClick = onPrimaryClick)
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        when (val s = uiState) {
            is PayUiState.Success -> PayContentView(
                data = s.order,
                payChannel = payChannel,
                payChannelChanged = payChannelChanged,
                modifier = Modifier.padding(paddingValues),
            )
            is PayUiState.Error -> Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            ) {
                Text(s.throwable?.message ?: "加载失败", style = MaterialTheme.typography.bodyMedium)
            }
            is PayUiState.Loading -> Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
