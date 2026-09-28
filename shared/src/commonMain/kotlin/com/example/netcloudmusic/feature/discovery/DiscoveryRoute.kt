package com.example.netcloudmusic.feature.discovery


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.message
import netcloudmusic.shared.generated.resources.search_tip

/**
 * 发现界面
 */
@Composable
fun DiscoveryRoute() {
    DiscoveryScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    toggleDrawer : () -> Unit = {} ,
    toSearch : () -> Unit = {}
) {
    Scaffold(
        topBar = {
            MyDiscoveryTopBar(toggleDrawer , toSearch)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) ,

            ) {
            item {
                Text("发现界面")
            }

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyDiscoveryTopBar(toggleDrawer : () -> Unit , toSearch : () -> Unit) {
    TopAppBar(navigationIcon = {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clickable { toggleDrawer() }
        )
    } , title = {
        Row(
            horizontalArrangement = Arrangement.Center ,
            verticalAlignment = Alignment.CenterVertically ,
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
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = stringResource(Res.string.search_tip),
                style = MaterialTheme.typography.bodySmall ,
                color = MaterialTheme.colorScheme.outline
            )
        }

    } , actions = {
        Image(
            painter = painterResource(Res.drawable.message),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(28.dp)
        )
    })
}
