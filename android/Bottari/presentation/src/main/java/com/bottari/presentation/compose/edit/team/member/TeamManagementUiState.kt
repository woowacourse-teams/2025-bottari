package com.bottari.presentation.compose.edit.team.member

import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel

data class TeamManagementUiState(
    val isLoading: Boolean = false,
    val inviteCode: String = "",
    val teamMemberHeadCount: Int = DEFAULT_VALUE,
    val maxHeadCount: Int = DEFAULT_VALUE,
    val members: List<TeamMemberUiModel> = emptyList(),
) {
    val isInviteCodeValid: Boolean = inviteCode.isNotBlank()

    companion object {
        private const val DEFAULT_VALUE = 0
    }
}
