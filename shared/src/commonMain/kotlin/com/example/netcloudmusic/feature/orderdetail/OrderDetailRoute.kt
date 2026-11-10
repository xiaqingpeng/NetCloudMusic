package com.example.netcloudmusic.feature.orderdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.design.component.MyErrorView
import com.example.netcloudmusic.core.design.component.MyLoading
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.design.theme.SpaceSmall
import com.example.netcloudmusic.core.model.Order
import com.example.netcloudmusic.core.util.SuperDateUtil
import com.example.netcloudmusic.core.util.formatPrice
import com.example.netcloudmusic.feature.confirmorder.ConfirmOrderAddressItem
import com.example.netcloudmusic.feature.confirmorder.ProductContainer
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.amount_pay
import netcloudmusic.shared.generated.resources.coupon
import netcloudmusic.shared.generated.resources.free_freight
import netcloudmusic.shared.generated.resources.go_pay
import netcloudmusic.shared.generated.resources.no_available
import netcloudmusic.shared.generated.resources.order_no
import netcloudmusic.shared.generated.resources.order_source
import netcloudmusic.shared.generated.resources.order_time
import netcloudmusic.shared.generated.resources.other_order_status
import netcloudmusic.shared.generated.resources.pay_channel
import netcloudmusic.shared.generated.resources.pay_platform
import netcloudmusic.shared.generated.resources.payment_before
import netcloudmusic.shared.generated.resources.product_total_price
import netcloudmusic.shared.generated.resources.refund
import netcloudmusic.shared.generated.resources.shipped_hint
import netcloudmusic.shared.generated.resources.shipping_fee
import netcloudmusic.shared.generated.resources.tab_mall_cart_selected
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OrderDetailRoute(
    viewModel: OrderDetailViewModel,
    toBack: () -> Unit,
    toPayOrder: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    OrderDetailScreen(uiState, toBack, toPayOrder, viewModel::loadData)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    uiState: OrderDetailUiState = OrderDetailUiState.Loading,
    toBack: () -> Unit = {},
    toPayOrder: (String) -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                toBack = toBack,
                title = "",
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        bottomBar = {
            if (uiState is OrderDetailUiState.Success) {
                when {
                    uiState.data.isWaitPay -> WaitPayBottomBar(uiState.data, toPayOrder)
                    uiState.data.isShipped -> WaitShipBottomBar()
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        when (val state = uiState) {
            OrderDetailUiState.Loading -> MyLoading(Modifier.padding(paddingValues))
            is OrderDetailUiState.Error -> MyErrorView(
                message = state.data,
                onRetryClick = onRetry,
                modifier = Modifier.padding(paddingValues),
            )
            is OrderDetailUiState.Success -> OrderDetailContent(
                data = state.data,
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

@Composable
private fun OrderDetailContent(data: Order, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(SpaceExtraMedium),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        OrderStatus(data)
        ConfirmOrderAddressItem(data.address)
        ProductContainer(data.carts.orEmpty())
        OtherContainer(data)
        InfoContainer(data)
    }
}

@Composable
private fun OrderStatus(data: Order) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(SpaceOuter),
    ) {
        Column(Modifier.weight(1f)) {
            Text(data.statusText, style = MaterialTheme.typography.headlineSmall, color = Color.White)
            val hint = when {
                data.isWaitPay -> stringResource(
                    Res.string.payment_before,
                    SuperDateUtil.paymentDeadline(data.created),
                )
                data.isShipped -> stringResource(Res.string.shipped_hint)
                else -> stringResource(Res.string.other_order_status)
            }
            Text(
                hint,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(top = SpaceMedium),
            )
        }
        androidx.compose.foundation.Image(
            painter = painterResource(Res.drawable.tab_mall_cart_selected),
            contentDescription = null,
            modifier = Modifier.size(50.dp),
        )
    }
}

@Composable
private fun OtherContainer(data: Order) {
    Column(
        verticalArrangement = Arrangement.spacedBy(SpaceSmall),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(SpaceOuter),
    ) {
        DetailInfoRow(stringResource(Res.string.product_total_price), "¥${formatPrice(data.totalPriceFloat)}")
        DetailInfoRow(stringResource(Res.string.shipping_fee), stringResource(Res.string.free_freight))
        DetailInfoRow(stringResource(Res.string.coupon), stringResource(Res.string.no_available))
        DetailInfoRow(
            stringResource(Res.string.amount_pay),
            "¥${formatPrice(data.priceFloat)}",
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun InfoContainer(data: Order) {
    Column(
        verticalArrangement = Arrangement.spacedBy(SpaceSmall),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = SpaceOuter, vertical = SpaceMedium),
    ) {
        DetailInfoRow(stringResource(Res.string.order_time), SuperDateUtil.yyyyMMddHHmmss(data.created))
        data.number?.takeIf { it.isNotBlank() }?.let {
            DetailInfoRow(stringResource(Res.string.order_no), it)
        }
        DetailInfoRow(stringResource(Res.string.order_source), data.getSourceFormat())
        DetailInfoRow(stringResource(Res.string.pay_platform), data.getOriginFormat())
        DetailInfoRow(stringResource(Res.string.pay_channel), data.getChannelFormat())
    }
}

@Composable
private fun DetailInfoRow(
    title: String,
    value: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.bodySmall, color = titleColor, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodySmall, color = valueColor)
    }
}

@Composable
private fun WaitPayBottomBar(data: Order, toPayOrder: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = SpaceOuter)
            .navigationBarsPadding(),
    ) {
        Text(stringResource(Res.string.amount_pay), style = MaterialTheme.typography.bodyMedium)
        Text(
            "¥${formatPrice(data.priceFloat)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = SpaceSmall),
        )
        Button(
            onClick = { toPayOrder(data.id) },
            modifier = Modifier.padding(start = SpaceMedium).weight(1f),
        ) {
            Text(stringResource(Res.string.go_pay))
        }
    }
}

@Composable
private fun WaitShipBottomBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = SpaceOuter)
            .navigationBarsPadding(),
    ) {
        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.refund))
        }
    }
}
