package com.example.netcloudmusic.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.feature.main.navigation.TopLevelDestination
import org.jetbrains.compose.resources.painterResource

/**
 * 底部导航栏
 * 
 * 在 KMP 中使用 Icon 组件，图标资源由各平台提供
 */
@Composable
fun MyNavigationBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (Int) -> Unit,
    currentDestination: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .navigationBarsPadding()
    ) {
        destinations.forEachIndexed { index, destination ->
            val selected = destination.route == currentDestination
            val color = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onNavigateToDestination(index)
                    }
                    .padding(7.dp)
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(
                        if (selected) destination.selectedIcon else destination.unselectedIcon
                    ),
                    contentDescription = destination.title,
                    modifier = Modifier.size(25.dp)
                )
                Text(
                    text = destination.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = color
                )
            }
        }
    }
}
