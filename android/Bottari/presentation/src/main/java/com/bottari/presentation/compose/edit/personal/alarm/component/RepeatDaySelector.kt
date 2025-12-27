package com.bottari.presentation.compose.edit.personal.alarm.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.alarm.RepeatDayUiModel
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun RepeatDaySelector(
    alarm: AlarmUiModel,
    onRepeatDaysChange: (RepeatDayUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        alarm.repeatDays.forEach { repeatDay ->
            val repeatDayColor =
                if (repeatDay.isChecked) BottariTheme.colors.green else BottariTheme.colors.white

            val repeatDayTextColor =
                if (repeatDay.isChecked) BottariTheme.colors.white else BottariTheme.colors.gray600

            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = if (repeatDay.isChecked) BottariTheme.colors.transparent else BottariTheme.colors.gray300,
                            shape = RoundedCornerShape(16.dp),
                        ).background(
                            color = repeatDayColor,
                            shape = RoundedCornerShape(16.dp),
                        ).clip(RoundedCornerShape(16.dp))
                        .clickable { onRepeatDaysChange(repeatDay) }
                        .padding(
                            vertical = BottariTheme.spacing.spaceMedium,
                            horizontal = BottariTheme.spacing.space2xSmall,
                        ),
            ) {
                Text(
                    text =
                        repeatDay.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault(),
                        ),
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
