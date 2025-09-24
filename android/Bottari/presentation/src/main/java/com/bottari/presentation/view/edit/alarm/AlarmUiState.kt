package com.bottari.presentation.view.edit.alarm

import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.alarm.AlarmUiModel.Companion.DEFAULT_ALARM_UI_MODEL

data class AlarmUiState(
    val isLoading: Boolean = false,
    val alarm: AlarmUiModel = DEFAULT_ALARM_UI_MODEL,
)
