package com.bottari.feature.personal.edit.alarm.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariToggleButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.model.alarm.AlarmUiModel
import com.bottari.core.ui.model.alarm.RepeatDayUiModel
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun RepeatDaySelector(
    alarm: AlarmUiModel,
    onRepeatDaysChange: (RepeatDayUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemShape = BottariTheme.shapes.radiusLarge

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        alarm.repeatDays.forEach { repeatDay ->
            val repeatDayColor =
                if (repeatDay.isChecked) BottariTheme.colors.green else BottariTheme.colors.white

            val repeatDayTextColor =
                if (repeatDay.isChecked) BottariTheme.colors.white else BottariTheme.colors.gray600

            BottariToggleButton(
                checked = repeatDay.isChecked,
                onCheckedChange = { onRepeatDaysChange(repeatDay) },
                shape = itemShape,
                modifier =
                    Modifier
                        .weight(1f)
                        .background(repeatDayColor, itemShape)
                        .border(
                            width = 1.dp,
                            color = BottariTheme.colors.gray200,
                            shape = itemShape,
                        ),
            ) {
                Text(
                    text =
                        repeatDay.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    color = repeatDayTextColor,
                    style = BottariTheme.typography.medium14.toTextStyle(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RepeatDaySelectorPreview() {
    BottariTheme {
        RepeatDaySelector(
            alarm = AlarmUiModel.DEFAULT_ALARM_UI_MODEL,
            onRepeatDaysChange = {},
        )
    }
}
