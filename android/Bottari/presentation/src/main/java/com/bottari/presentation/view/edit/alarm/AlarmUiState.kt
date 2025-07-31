package com.bottari.presentation.view.edit.alarm

import com.bottari.presentation.model.AlarmUiModel

data class AlarmUiState(
    val isLoading: Boolean = false,
    val alarm: AlarmUiModel,
)
