package com.bottari.presentation.view.edit.alarm

import com.bottari.presentation.model.alarm.NotificationUiModel

sealed interface AlarmUiEvent {
    data object FetchAlarmFailure : AlarmUiEvent

    data class SaveAlarmSuccess(
        val notification: NotificationUiModel,
    ) : AlarmUiEvent

    data object SaveAlarmFailure : AlarmUiEvent
}
