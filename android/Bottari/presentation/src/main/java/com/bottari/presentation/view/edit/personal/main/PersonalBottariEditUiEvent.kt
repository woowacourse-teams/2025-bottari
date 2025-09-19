package com.bottari.presentation.view.edit.personal.main

sealed interface PersonalBottariEditUiEvent {
    sealed interface FetchBottariFailure : PersonalBottariEditUiEvent {
        data object PermissionException : FetchBottariFailure

        data object NotFoundException : FetchBottariFailure

        data object UnexpectedException : FetchBottariFailure
    }

    data object CreateTemplateSuccess : PersonalBottariEditUiEvent

    sealed interface CreateTemplateFailure : PersonalBottariEditUiEvent {
        data object InvalidException : CreateTemplateFailure

        data object NotFoundException : CreateTemplateFailure

        data object UnexpectedException : CreateTemplateFailure
    }

    sealed interface ToggleAlarmStateFailure : PersonalBottariEditUiEvent {
        data object NotFoundException : ToggleAlarmStateFailure

        data object DuplicatedException : ToggleAlarmStateFailure

        data object UnexpectedException : ToggleAlarmStateFailure
    }
}
