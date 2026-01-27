package com.bottari.presentation.compose.edit.team.main

sealed interface TeamBottariEditUiEvent {
    data object FetchTeamBottariDetailFailure : TeamBottariEditUiEvent

    data object ToggleAlarmStateFailure : TeamBottariEditUiEvent
}
