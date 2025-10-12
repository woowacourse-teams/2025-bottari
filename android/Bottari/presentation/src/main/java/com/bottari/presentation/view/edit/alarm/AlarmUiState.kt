package com.bottari.presentation.view.edit.alarm

import com.bottari.presentation.model.alarm.AlarmTypeUiModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import java.time.LocalDateTime

data class AlarmUiState(
    val isLoading: Boolean = false,
    val alarm: AlarmUiModel? = null,
) {
    private val isRepeatWithoutDays: Boolean =
        alarm?.let { alarm ->
            alarm.type == AlarmTypeUiModel.REPEAT && alarm.repeatDays.all { !it.isChecked }
        } ?: true

    private val isBeforeTime: Boolean =
        alarm?.let { alarm ->
            alarm.type == AlarmTypeUiModel.NON_REPEAT &&
                LocalDateTime.of(alarm.date, alarm.time).isBefore(LocalDateTime.now())
        } ?: true

    val isSavable: Boolean = !isRepeatWithoutDays && !isBeforeTime
}
