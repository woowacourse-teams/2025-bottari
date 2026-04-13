package com.bottari.feature.personal.edit.impl.alarm

sealed interface AlarmUiEvent {
    data object FetchAlarmFailure : AlarmUiEvent

    data object SaveAlarmFailure : AlarmUiEvent
}
