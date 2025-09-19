package com.bottari.presentation.view.home.personal

sealed interface BottariUiEvent {
    data object BottariDeleteSuccess : BottariUiEvent

    sealed interface BottariDeleteFailure : BottariUiEvent {
        data object NotFoundException : BottariDeleteFailure

        data object PermissionException : BottariDeleteFailure

        data object UnexpectedException : BottariDeleteFailure
    }

    sealed interface FetchBottariesFailure : BottariUiEvent {
        data object NotFoundException : FetchBottariesFailure

        data object UnexpectedException : FetchBottariesFailure
    }

    data object DeleteNotificationFailure : BottariUiEvent
}
