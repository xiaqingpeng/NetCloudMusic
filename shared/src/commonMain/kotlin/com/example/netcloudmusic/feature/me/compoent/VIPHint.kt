package com.example.netcloudmusic.feature.me.compoent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.theme.SpaceLarge
import com.example.netcloudmusic.core.design.theme.SpaceSmallWidth
import com.example.netcloudmusic.core.design.theme.smallTopRoundedCornerShape
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.mall_vip_hint
import netcloudmusic.shared.generated.resources.super_vip
import org.jetbrains.compose.resources.stringResource

@Composable
fun VIPHint() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 35.dp)
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = smallTopRoundedCornerShape,
            )
            .fillMaxWidth()
            .padding(SpaceLarge),
    ) {
        Icon(
            imageVector = Icons.Default.WorkspacePremium,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(25.dp),
        )
        SpaceSmallWidth()
        Text(
            text = stringResource(Res.string.super_vip),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = stringResource(Res.string.mall_vip_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(14.dp),
        )
    }
}
