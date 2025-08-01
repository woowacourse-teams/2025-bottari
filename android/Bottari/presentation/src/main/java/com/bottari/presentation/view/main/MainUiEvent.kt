package com.bottari.presentation.view.main

sealed interface MainUiEvent {
    data object RegisterFailure : MainUiEvent

    data object LoginSuccess : MainUiEvent

    data object LoginFailure : MainUiEvent

    data object GetPermissionFlagFailure : MainUiEvent

    data object IncompletePermissionFlow : MainUiEvent
}
