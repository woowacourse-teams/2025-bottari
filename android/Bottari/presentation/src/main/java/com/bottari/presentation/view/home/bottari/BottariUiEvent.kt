package com.bottari.presentation.view.home.bottari

sealed interface BottariUiEvent {
    data object BottariDeleteSuccess : BottariUiEvent

    data object BottariDeleteFailure : BottariUiEvent

    data object FetchBottariesFailure : BottariUiEvent
}
