package com.bottari.feature.invite

sealed interface InviteUiEvent {
    data object JoinTeamBottariSuccess : InviteUiEvent

    data object JoinTeamBottariFailure : InviteUiEvent
}
