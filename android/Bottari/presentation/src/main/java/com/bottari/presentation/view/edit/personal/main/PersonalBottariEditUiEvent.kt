package com.bottari.presentation.view.edit.personal.main

sealed interface PersonalBottariEditUiEvent {
    data object FetchBottariFailure : PersonalBottariEditUiEvent

    data object CreateTemplateSuccess : PersonalBottariEditUiEvent

    data object CreateTemplateFailure : PersonalBottariEditUiEvent

    data object ToggleAlarmStateFailure : PersonalBottariEditUiEvent
}
