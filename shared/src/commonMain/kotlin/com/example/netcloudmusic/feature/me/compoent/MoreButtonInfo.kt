package com.example.netcloudmusic.feature.me.compoent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.theme.LocalDividerColor
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceSmallHeight
import com.example.netcloudmusic.feature.me.model.ButtonControl
import com.example.netcloudmusic.core.util.ResourceUtil
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.more_service
import netcloudmusic.shared.generated.resources.show_more
import org.jetbrains.compose.resources.stringResource@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoreButtonInfo(
    buttonControls: List<ButtonControl>,
    modifier: Modifier = Modifier,
    onItemClick: (ButtonControl) -> Unit,
) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(bottom = SpaceMedium)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = SpaceMedium),
            ) {
                Text(
                    text = stringResource(Res.string.more_service),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )
                Row(modifier = Modifier.padding(vertical = SpaceExtraMedium)) {
                    Text(
                        text = stringResource(Res.string.show_more),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.outline,
                    )
                }
            }

            Spacer(Modifier.fillMaxWidth().height(1.dp).background(LocalDividerColor.current))

            FlowRow(
                maxItemsInEachRow = 4,
                modifier = Modifier.fillMaxWidth(),
            ) {
                buttonControls.forEach { button ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onItemClick(button) }
                            .padding(vertical = 10.dp),
                    ) {
                        AsyncImage(
                            model = ResourceUtil.r(button.icon),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                        )
                        SpaceSmallHeight()
                        Text(
                            text = button.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
