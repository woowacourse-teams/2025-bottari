package com.bottari.presentation.view.edit.alarm

sealed interface AlarmUiEvent {
    data object FetchAlarmFailure : AlarmUiEvent

    data object SaveAlarmSuccess : AlarmUiEvent

    data object SaveAlarmFailure : AlarmUiEvent
}
