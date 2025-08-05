package com.bottari.presentation.view.edit.alarm

import com.bottari.presentation.model.NotificationUiModel

sealed interface AlarmUiEvent {
    data class AlarmCreateSuccess(
        val notification: NotificationUiModel,
    ) : AlarmUiEvent

    data object AlarmCreateFailure : AlarmUiEvent

    data class AlarmSaveSuccess(
        val notification: NotificationUiModel,
    ) : AlarmUiEvent

    data object AlarmSaveFailure : AlarmUiEvent
}
