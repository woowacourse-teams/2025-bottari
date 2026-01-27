package com.bottari.presentation.compose.edit.team.member

sealed interface TeamManagementUiEvent {
    data object FetchTeamMembersFailure : TeamManagementUiEvent
}
