package com.bottari.presentation.view.edit.team.main

sealed interface TeamBottariEditUiEvent {
    data object FetchTeamBottariDetailFailure : TeamBottariEditUiEvent

    data object ToggleAlarmStateFailure : TeamBottariEditUiEvent
}
