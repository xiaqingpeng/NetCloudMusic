package com.example.netcloudmusic.core.design.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.example.netcloudmusic.core.design.theme.Space3XLarge
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.design.theme.SpaceLarge
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.gender_female
import netcloudmusic.shared.generated.resources.gender_male
import netcloudmusic.shared.generated.resources.gender_secret
import netcloudmusic.shared.generated.resources.select_gender
import org.jetbrains.compose.resources.stringResource

/**
 * 性别选择对话框
 *
 * @param value 当前性别值（0=保密，10=男，20=女）
 * @param onSelectChange 选中新性别后回调，参数为新性别 Int 值
 * @param onDismissRequest 点击对话框外部关闭回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGenderDialog(
    title: String = stringResource(Res.string.select_gender),
    value: Int = 0,
    onSelectChange: (Int) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    // 性别选项（value -> label），直接在代码中定义，无需 arrays.xml
    val secretLabel = stringResource(Res.string.gender_secret)
    val maleLabel = stringResource(Res.string.gender_male)
    val femaleLabel = stringResource(Res.string.gender_female)
    val genderOptions = remember(secretLabel, maleLabel, femaleLabel) {
        listOf(
            0 to secretLabel,
            10 to maleLabel,
            20 to femaleLabel,
        )
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpaceLarge),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.padding(top = Space3XLarge))

                // 逐项渲染单选项
                genderOptions.forEach { (genderValue, genderLabel) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (genderValue == value),
                                onClick = { onSelectChange(genderValue) },
                            )
                            .padding(vertical = SpaceExtraMedium),
                    ) {
                        RadioButton(
                            selected = (genderValue == value),
                            onClick = null, // 点击由外层 Row 处理
                        )
                        Text(
                            text = genderLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = SpaceOuter),
                        )
                    }
                }
            }
        }
    }
}
