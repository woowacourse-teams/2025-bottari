package com.bottari.presentation.view.home.team

sealed interface TeamBottariUiEvent {
    data object BottariDeleteSuccess : TeamBottariUiEvent

    sealed interface BottariDeleteFailure : TeamBottariUiEvent {
        data object PermissionException : BottariDeleteFailure

        data object NotFoundException : BottariDeleteFailure

        data object UnexpectedException : BottariDeleteFailure
    }

    sealed interface FetchBottariesFailure : TeamBottariUiEvent {
        data object NotFoundException : FetchBottariesFailure

        data object UnexpectedException : FetchBottariesFailure
    }
}
