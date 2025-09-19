package com.bottari.presentation.view.edit.alarm

import com.bottari.presentation.model.alarm.NotificationUiModel

sealed interface AlarmUiEvent {
    data class AlarmCreateSuccess(
        val notification: NotificationUiModel,
    ) : AlarmUiEvent

    sealed interface AlarmCreateFailure : AlarmUiEvent {
        data object InvalidException : AlarmCreateFailure

        data object NotFoundException : AlarmCreateFailure

        data object UnexpectedException : AlarmCreateFailure
    }

    data class AlarmSaveSuccess(
        val notification: NotificationUiModel,
    ) : AlarmUiEvent

    sealed interface AlarmSaveFailure : AlarmUiEvent {
        data object InvalidException : AlarmCreateFailure

        data object NotFoundException : AlarmCreateFailure

        data object UnexpectedException : AlarmCreateFailure
    }
}
