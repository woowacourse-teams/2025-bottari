package com.bottari.presentation.view.edit.team.main

sealed interface TeamBottariEditUiEvent {
    data object FetchTeamBottariFailure : TeamBottariEditUiEvent

    data object ToggleAlarmStateFailure : TeamBottariEditUiEvent
}
