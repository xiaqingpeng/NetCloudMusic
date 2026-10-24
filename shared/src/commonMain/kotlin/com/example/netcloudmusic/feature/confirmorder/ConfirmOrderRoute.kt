package com.example.netcloudmusic.feature.confirmorder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.model.Address
import com.example.netcloudmusic.core.model.Cart
import com.example.netcloudmusic.core.util.ResourceUtil
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.placeholder
import netcloudmusic.shared.generated.resources.select_address
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

const val SELECT_ADDRESS_RESULT = "select_address_result"

@Suppress("ParamsComparedByRef")
@Composable
fun ConfirmOrderRoute(
    toBack: () -> Unit,
    toSelectAddress: () -> Unit,
    toPayOrder: (orderId: String) -> Unit,
    viewModel: ConfirmOrderViewModel,
    navController: NavHostController? = null,
) {
    val uiState by viewModel.uiState.collectAsState()
    val tipErrorRes by viewModel.tipErrorRes.collectAsState()
    val toPay = viewModel.toPay.value

    // 监听地址选择结果
    LaunchedEffect(navController) {
        navController?.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<String?>(SELECT_ADDRESS_RESULT, null)
            ?.collect { addressId ->
                if (addressId != null) {
                    viewModel.onAddressChange(addressId)
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<String>(SELECT_ADDRESS_RESULT)
                }
            }
    }

    // 跳转到支付页面
    LaunchedEffect(toPay) {
        toPay?.let {
            toPayOrder(it)
            viewModel.clearToPay()
        }
    }

    // 错误提示
    LaunchedEffect(tipErrorRes) {
        tipErrorRes?.let {
            // TODO: 显示错误提示
            viewModel.clearTipErrorRes()
        }
    }

    ConfirmOrderScreen(
        uiState = uiState,
        toBack = toBack,
        toSelectAddress = toSelectAddress,
        onPrimaryClick = viewModel::onPrimaryClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmOrderScreen(
    uiState: ConfirmOrderUiState = ConfirmOrderUiState.Loading,
    toBack: () -> Unit = {},
    toSelectAddress: () -> Unit = {},
    onPrimaryClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                toBack = toBack,
                title = "确认订单",
            )
        },
        bottomBar = {
            when (uiState) {
                is ConfirmOrderUiState.Success -> {
                    Button(
                        onClick = onPrimaryClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("提交订单 ￥${uiState.confirmOrder.priceFloat}")
                    }
                }
                else -> {}
            }
        },
    ) { paddingValues ->
        when (uiState) {
            is ConfirmOrderUiState.Success -> {
                ConfirmOrderContent(
                    confirmOrder = uiState.confirmOrder,
                    toSelectAddress = toSelectAddress,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is ConfirmOrderUiState.Error -> {
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
            is ConfirmOrderUiState.Loading -> {
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
fun ConfirmOrderContent(
    confirmOrder: com.example.netcloudmusic.core.model.ConfirmOrderResponse,
    toSelectAddress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // 地址选择
        ConfirmOrderAddressItem(
            data = confirmOrder.address,
            modifier = Modifier.clickable { toSelectAddress() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 商品列表
        confirmOrder.carts.forEach { cart ->
            ConfirmOrderCartItem(data = cart)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 价格信息
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("商品总价", style = MaterialTheme.typography.bodyMedium)
                Text("￥${confirmOrder.totalPriceFloat}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("实付金额", style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text("￥${confirmOrder.priceFloat}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun ConfirmOrderAddressItem(data: Address? = null, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            if (data != null) {
                Text(
                    text = "${data.name} ${data.telephone}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${data.province} ${data.city} ${data.area}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = data.detail,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            } else {
                Text(
                    text = stringResource(Res.string.select_address),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
        )
    }
}

@Composable
fun ConfirmOrderCartItem(data: Cart, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 商品图片
        AsyncImage(
            model = data.product?.icon?.let(ResourceUtil::r),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp),
            placeholder = painterResource(Res.drawable.placeholder),
            error = painterResource(Res.drawable.placeholder),
            onError = {
                data.product?.icon?.let {
                    println("Confirm order image load failed: ${ResourceUtil.r(it)}")
                }
            },
        )

        // 商品信息
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = data.product?.title ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 规格
            data.stock?.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 价格和数量
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "￥${data.product?.priceFloat ?: 0.0}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "x${data.count}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
