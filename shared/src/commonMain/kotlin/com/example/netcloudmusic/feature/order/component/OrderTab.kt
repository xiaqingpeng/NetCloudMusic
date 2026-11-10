package com.example.netcloudmusic.feature.order.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.all_order
import netcloudmusic.shared.generated.resources.wait_comment
import netcloudmusic.shared.generated.resources.wait_pay
import netcloudmusic.shared.generated.resources.wait_received
import org.jetbrains.compose.resources.stringResource

/**
 * 订单列表顶部 Tab：全部 / 待付款 / 待收货 / 待评价
 */
@Composable
fun OrderTab(
    selectedIndex: Int,
    tabChanged: (Int) -> Unit,
) {
    val tabs = listOf(
        Res.string.all_order,
        Res.string.wait_pay,
        Res.string.wait_received,
        Res.string.wait_comment,
    )
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedIndex])
                    .height(2.dp),
            )
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                text = { Text(stringResource(title)) },
                selected = selectedIndex == index,
                onClick = { tabChanged(index) },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
