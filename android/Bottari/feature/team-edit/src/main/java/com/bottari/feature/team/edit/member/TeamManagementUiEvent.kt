package com.bottari.feature.team.edit.member

sealed interface TeamManagementUiEvent {
    data object FetchTeamMembersFailure : TeamManagementUiEvent
}
