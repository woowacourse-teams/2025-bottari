package com.bottari.feature.team.edit.member

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.bottari.team.member.TeamMemberUiModel

@Immutable
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
