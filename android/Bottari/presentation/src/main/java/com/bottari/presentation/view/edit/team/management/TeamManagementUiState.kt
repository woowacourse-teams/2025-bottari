package com.bottari.presentation.view.edit.team.management

import com.bottari.presentation.model.TeamMemberUiModel

data class TeamManagementUiState(
    val isLoading: Boolean = false,
    val inviteCode: String = "",
    val teamMemberHeadCount: Int = DEFAULT_VALUE,
    val maxHeadCount: Int = DEFAULT_VALUE,
    val members: List<TeamMemberUiModel> = emptyList(),
) {
    companion object {
        private const val DEFAULT_VALUE = 0
    }
}
