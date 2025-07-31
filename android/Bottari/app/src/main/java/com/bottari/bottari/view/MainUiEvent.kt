package com.bottari.bottari.view

sealed interface MainUiEvent {
    data object RegisterFailure : MainUiEvent

    data object LoginSuccess : MainUiEvent

    data object LoginFailure : MainUiEvent
}
