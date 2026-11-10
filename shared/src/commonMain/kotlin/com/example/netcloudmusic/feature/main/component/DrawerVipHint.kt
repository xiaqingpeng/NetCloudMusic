package com.example.netcloudmusic.feature.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DrawerVipHint() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFF4E5))
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "开通会员",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = {}) { Text("会员中心") }
        }
        Text("畅听会员歌曲、专属音质和更多服务", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(12.dp))
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFE9D8BD)),
        )
        Spacer(Modifier.height(8.dp))
        Text("低至 ¥ 0.5 / 天", style = MaterialTheme.typography.bodySmall)
    }
}
