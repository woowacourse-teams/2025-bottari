package com.bottari.feature.team.checklist.member

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.bottari.team.member.TeamMemberStatusUiModel

@Immutable
data class ComposeTeamMembersStatusUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val membersStatus: List<TeamMemberStatusUiModel> = emptyList(),
    val myId: Long = -1L,
    val selectedMember: TeamMemberStatusUiModel? = null,
) {
    val isInitialLoading: Boolean = isLoading && isFetched.not()
    val checkedMembers: List<TeamMemberStatusUiModel> = membersStatus.filter { it.isAllChecked }
    val uncheckedMembers: List<TeamMemberStatusUiModel> = membersStatus.filter { it.isAllChecked.not() }
}
