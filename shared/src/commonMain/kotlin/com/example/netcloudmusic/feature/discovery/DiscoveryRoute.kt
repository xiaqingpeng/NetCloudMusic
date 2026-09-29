package com.example.netcloudmusic.feature.discovery


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.design.theme.SpaceExtraSmall
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.ui.PreviewParameterData
import com.example.netcloudmusic.feature.compoent.ItemProduct
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.message
import netcloudmusic.shared.generated.resources.search_tip
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 发现界面
 */
@Composable
fun DiscoveryRoute() {
    DiscoveryScreen(
        products = PreviewParameterData.products,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    products: List<Product> = emptyList(),
    toggleDrawer : () -> Unit = {} ,
    toSearch : () -> Unit = {}
) {
//    val context = LocalContext.current
//    val products = remember(context) {
//        context.assets.open("product.json").bufferedReader().use {
//            Json.decodeFromString<ProductResponse>(it.readText()).data
//        }
//    }

    Scaffold(
        topBar = {
            MyDiscoveryTopBar(toggleDrawer , toSearch)
        },
        //排除底部导航栏边距
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
//            .exclude(WindowInsets.ime)
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = SpaceExtraMedium,
                top = paddingValues.calculateTopPadding() + SpaceExtraMedium,
                end = SpaceExtraMedium,
                bottom = paddingValues.calculateBottomPadding() + SpaceExtraMedium
            ),
            verticalArrangement = Arrangement.spacedBy(SpaceExtraSmall),
            modifier = Modifier
                .fillMaxSize()

        ) {
            items(products) { product ->
                ItemProduct(
                    modifier = Modifier,
                    data = product
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyDiscoveryTopBar(toggleDrawer : () -> Unit , toSearch : () -> Unit) {
    TopAppBar(navigationIcon = {
        IconButton(onClick = toggleDrawer) {
            Icon(
                imageVector = Icons.Default.Menu ,
                contentDescription = null ,
                modifier = Modifier.size(36.dp)
            )
        }
    } , title = {
        SearchPlaceHolder(toSearch)
    } , actions = {
        Icon(
            painter = painterResource(Res.drawable.message) ,
            contentDescription = null ,
            tint = MaterialTheme.colorScheme.onSurface ,
            modifier = Modifier
                .padding(horizontal = SpaceExtraMedium)
                .size(28.dp)

        )
    })
}

@Composable
private fun SearchPlaceHolder(toSearch: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                toSearch()
            }

    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .size(18.dp)

        )
        Text(
            text = stringResource(Res.string.search_tip),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
