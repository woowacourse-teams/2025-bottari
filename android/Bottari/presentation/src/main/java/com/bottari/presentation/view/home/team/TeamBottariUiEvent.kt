package com.bottari.presentation.view.home.team

sealed interface TeamBottariUiEvent {
    data object BottariDeleteSuccess : TeamBottariUiEvent

    data object BottariDeleteFailure : TeamBottariUiEvent

    data object FetchBottariesFailure : TeamBottariUiEvent
}
