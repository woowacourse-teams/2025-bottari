package com.bottari.bottari.view.splash

sealed interface SplashUiEvent {
    data object RegisterFailure : SplashUiEvent

    data class LoginSuccess(
        val permissionFlag: Boolean,
    ) : SplashUiEvent

    data class Offline(
        val permissionFlag: Boolean,
    ) : SplashUiEvent

    data object LoginFailure : SplashUiEvent

    data object GetPermissionFlagFailure : SplashUiEvent

    data object SavePermissionFlagFailure : SplashUiEvent

    data object IncompletePermissionFlow : SplashUiEvent

    data object ForceUpdate : SplashUiEvent
}
