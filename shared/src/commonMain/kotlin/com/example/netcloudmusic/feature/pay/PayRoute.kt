package com.example.netcloudmusic.feature.pay

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.util.Constant
import com.example.netcloudmusic.feature.pay.component.PayBottomBar
import com.example.netcloudmusic.feature.pay.component.PayChannelSelector
import com.example.netcloudmusic.feature.pay.component.PayPriceDisplay
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.pay_order
import org.jetbrains.compose.resources.stringResource

@Composable
fun PayRoute(
    toBack: () -> Unit,
    toOrderDetail: (orderId: String) -> Unit,
    viewModel: PayViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    PayScreen(
        uiState = uiState,
        toBack = toBack,
        payChannel = viewModel.payChannel,
        payChannelChanged = viewModel::payChannelChanged,
        onPrimaryClick = viewModel::onPrimaryClick,
    )

    // 支付结果处理
    LaunchedEffect(viewModel.nextValue) {
        viewModel.nextValue?.let {
            when (it.style) {
                Constant.VALUE0 -> {
                    // 从确认订单跳转过来，关闭当前界面并跳转到订单详情
                    toOrderDetail(it.orderId)
                }
                else -> {
                    toBack()
                }
            }
        }
    }
}

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
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        when (uiState) {
            is PayUiState.Success -> {
                PayContent(
                    order = uiState.order,
                    payChannel = payChannel,
                    payChannelChanged = payChannelChanged,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is PayUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.throwable?.message ?: "加载失败",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            is PayUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "加载中...")
                }
            }
        }
    }
}

@Composable
fun PayContent(
    order: com.example.netcloudmusic.core.model.Order,
    payChannel: Int,
    payChannelChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // 价格显示
        PayPriceDisplay(priceFloat = order.priceFloat)

        Spacer(modifier = Modifier.height(16.dp))

        // 支付渠道选择
        PayChannelSelector(
            payChannel = payChannel,
            payChannelChanged = payChannelChanged
        )
    }
}
