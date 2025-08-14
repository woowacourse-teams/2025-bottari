package com.bottari.presentation.view.checklist.team.member

import com.bottari.presentation.model.TeamMemberStatusUiModel

data class TeamMembersStatusUiState(
    val isLoading: Boolean = false,
    val membersStatus: List<TeamMemberStatusUiModel> = emptyList(),
)
