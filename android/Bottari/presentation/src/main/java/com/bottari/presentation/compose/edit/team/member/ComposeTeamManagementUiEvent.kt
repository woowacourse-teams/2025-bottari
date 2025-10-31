package com.bottari.presentation.compose.edit.team.member

sealed interface ComposeTeamManagementUiEvent {
    data object FetchTeamMembersFailure : ComposeTeamManagementUiEvent
}
