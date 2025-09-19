package com.bottari.presentation.view.invite

sealed interface InviteUiEvent {
    data object JoinTeamBottariSuccess : InviteUiEvent

    sealed interface JoinTeamBottariFailure : InviteUiEvent {
        data object NotFoundException : JoinTeamBottariFailure

        data object DuplicatedException : JoinTeamBottariFailure

        data object UnexpectedException : JoinTeamBottariFailure
    }
}
