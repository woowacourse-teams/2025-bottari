package com.bottari.feature.personal.edit.alarm.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.model.alarm.AlarmTypeUiModel
import com.bottari.core.ui.model.alarm.AlarmUiModel
import com.bottari.core.ui.model.alarm.RepeatDayUiModel
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DateSelector(
    alarm: AlarmUiModel,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text =
                when {
                    alarm.isRepeatEveryDay -> "매일 반복"
                    alarm.type == AlarmTypeUiModel.REPEAT -> formatRepeatDays(alarm.repeatDays)
                    else -> alarm.date.toString()
                },
            modifier = Modifier.weight(1f),
            style = BottariTheme.typography.semiBold24.toTextStyle(),
        )

        BottariIconButton(onClick = onCalendarClick) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "날짜 선택",
            )
        }
    }
}

private fun formatRepeatDays(repeatDays: List<RepeatDayUiModel>): String {
    val checkedDays = repeatDays.filter { repeatDay -> repeatDay.isChecked }
    return checkedDays.joinToString { checkedDay ->
        checkedDay.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }
}

@Preview(showBackground = true)
@Composable
private fun DateSelectorPreview() {
    BottariTheme {
        DateSelector(
            alarm = AlarmUiModel.DEFAULT_ALARM_UI_MODEL,
            onCalendarClick = {},
        )
    }
}
