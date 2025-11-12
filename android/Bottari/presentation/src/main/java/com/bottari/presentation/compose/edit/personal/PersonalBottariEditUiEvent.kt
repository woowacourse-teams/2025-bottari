package com.bottari.presentation.compose.edit.personal

sealed interface PersonalBottariEditUiEvent {
    data object FindBottariFailure : PersonalBottariEditUiEvent

    data object CreateTemplateSuccess : PersonalBottariEditUiEvent

    data object CreateTemplateFailure : PersonalBottariEditUiEvent

    data object ToggleAlarmStateFailure : PersonalBottariEditUiEvent
}
