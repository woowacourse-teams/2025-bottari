package com.bottari.presentation.compose.edit.personal.alarm

import androidx.compose.runtime.Immutable
import com.bottari.presentation.model.alarm.AlarmTypeUiModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import java.time.LocalDateTime

@Immutable
data class AlarmUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val alarm: AlarmUiModel = AlarmUiModel.DEFAULT_ALARM_UI_MODEL,
) {
    private val isRepeatWithoutDays: Boolean =
        alarm.type == AlarmTypeUiModel.REPEAT && alarm.repeatDays.all { !it.isChecked }

    private val isBeforeTime: Boolean =
        alarm.type == AlarmTypeUiModel.NON_REPEAT &&
            LocalDateTime.of(alarm.date, alarm.time).isBefore(LocalDateTime.now())

    val isSavable: Boolean = !isRepeatWithoutDays && !isBeforeTime
}
