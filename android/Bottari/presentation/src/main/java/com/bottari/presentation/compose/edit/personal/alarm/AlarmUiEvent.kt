package com.bottari.presentation.compose.edit.personal.alarm

sealed interface AlarmUiEvent {
    data object FetchAlarmFailure : AlarmUiEvent

    data object SaveAlarmFailure : AlarmUiEvent
}
