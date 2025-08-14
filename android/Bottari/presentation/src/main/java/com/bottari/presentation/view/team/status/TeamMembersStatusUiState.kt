package com.bottari.presentation.view.team.status

import com.bottari.presentation.model.TeamMemberStatusUiModel

data class TeamMembersStatusUiState(
    val isLoading: Boolean = false,
    val membersStatus: List<TeamMemberStatusUiModel> = emptyList(),
)
