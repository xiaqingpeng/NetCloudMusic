package com.example.netcloudmusic.core.design.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.cancel
import netcloudmusic.shared.generated.resources.confirm
import netcloudmusic.shared.generated.resources.select_birthday
import org.jetbrains.compose.resources.stringResource

/**
 * 生日日期选择弹窗
 *
 * @param initialDate  当前已选日期，格式 yyyy-MM-dd，为空则默认今天
 * @param onConfirm    确认回调，参数为格式化后的日期字符串 yyyy-MM-dd
 * @param onDismiss    取消/关闭回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    initialDate: String? = null,
    onConfirm: (String) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    // 将 yyyy-MM-dd 格式的日期字符串解析为 UTC 毫秒时间戳
    val initialMillis: Long? = runCatching {
        initialDate?.let { dateStr ->
            val parts = dateStr.split("-")
            if (parts.size == 3) {
                val localDate = LocalDate(
                    year = parts[0].toInt(),
                    monthNumber = parts[1].toInt(),
                    dayOfMonth = parts[2].toInt()
                )
                // 转换为 UTC 00:00:00 的毫秒时间戳
                val instant = Instant.fromEpochSeconds(
                    localDate.toEpochDays() * 86400L
                )
                instant.toEpochMilliseconds()
            } else null
        }
    }.getOrNull()

    val state = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = state.selectedDateMillis
                    if (millis != null) {
                        val instant = Instant.fromEpochMilliseconds(millis)
                        val localDate = instant.toLocalDateTime(TimeZone.UTC).date
                        val formatted = "${localDate.year}-" +
                            "${localDate.monthNumber.toString().padStart(2, '0')}-" +
                            "${localDate.dayOfMonth.toString().padStart(2, '0')}"
                        onConfirm(formatted)
                    } else {
                        onDismiss()
                    }
                },
            ) {
                Text(stringResource(Res.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel))
            }
        },
    ) {
        DatePicker(
            state = state,
            title = {
                Text(
                    text = stringResource(Res.string.select_birthday),
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                )
            },
        )
    }
}
