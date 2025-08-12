package com.bottari.presentation.view.home.personal

sealed interface BottariUiEvent {
    data object BottariDeleteSuccess : BottariUiEvent

    data object BottariDeleteFailure : BottariUiEvent

    data object FetchBottariesFailure : BottariUiEvent
}
