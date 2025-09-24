package com.bottari.presentation.view.edit.alarm

import com.bottari.presentation.model.alarm.AlarmTypeUiModel
import com.bottari.presentation.model.alarm.AlarmUiModel

data class AlarmUiState(
    val isLoading: Boolean = false,
    val alarm: AlarmUiModel? = null,
) {
    val isRepeatWithoutDays: Boolean =
        alarm?.let { alarm ->
            alarm.type == AlarmTypeUiModel.REPEAT && alarm.repeatDays.all { !it.isChecked }
        } ?: true
}
