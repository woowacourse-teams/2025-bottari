package com.bottari.presentation.view.checklist.team.main.status

sealed interface TeamBottariStatusUiEvent {
    data object FetchTeamBottariStatusFailure : TeamBottariStatusUiEvent

    data object SendRemindSuccess : TeamBottariStatusUiEvent

    data object SendRemindFailure : TeamBottariStatusUiEvent
}
