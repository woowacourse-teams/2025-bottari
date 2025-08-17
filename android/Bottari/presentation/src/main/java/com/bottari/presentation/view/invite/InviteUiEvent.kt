package com.bottari.presentation.view.invite

sealed interface InviteUiEvent {
    data object JoinTeamBottariSuccess : InviteUiEvent

    data object JoinTeamBottariFailure : InviteUiEvent
}
