package com.bottari.feature.team.edit.main

sealed interface TeamBottariEditUiEvent {
    data object FetchTeamBottariDetailFailure : TeamBottariEditUiEvent

    data object ToggleAlarmStateFailure : TeamBottariEditUiEvent
}
