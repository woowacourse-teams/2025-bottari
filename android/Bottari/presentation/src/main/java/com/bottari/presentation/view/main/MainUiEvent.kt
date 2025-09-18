package com.bottari.presentation.view.main

sealed interface MainUiEvent {
    data object AuthorizeFailure : MainUiEvent

    sealed interface RegisterFailure : MainUiEvent {
        data object InvalidException : RegisterFailure

        data object DuplicatedException : RegisterFailure

        data object UnexpectedException : RegisterFailure
    }

    data class LoginSuccess(
        val permissionFlag: Boolean,
    ) : MainUiEvent

    data object IncompletePermissionFlow : MainUiEvent

    data object ForceUpdate : MainUiEvent
}
