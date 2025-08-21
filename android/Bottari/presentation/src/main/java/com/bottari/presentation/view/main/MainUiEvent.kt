package com.bottari.presentation.view.main

sealed interface MainUiEvent {
    data object RegisterFailure : MainUiEvent

    data class LoginSuccess(
        val permissionFlag: Boolean,
    ) : MainUiEvent

    data object LoginFailure : MainUiEvent

    data object GetPermissionFlagFailure : MainUiEvent

    data object SavePermissionFlagFailure : MainUiEvent

    data object IncompletePermissionFlow : MainUiEvent

    data object ForceUpdate : MainUiEvent
}
