package com.bottari.presentation.view.edit.alarm

sealed interface AlarmUiEvent {
    data object AlarmCreateSuccess : AlarmUiEvent

    data object AlarmCreateFailure : AlarmUiEvent

    data object AlarmSaveSuccess : AlarmUiEvent

    data object AlarmSaveFailure : AlarmUiEvent
}
