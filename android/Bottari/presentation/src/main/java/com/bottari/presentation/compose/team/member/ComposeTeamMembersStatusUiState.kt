package com.bottari.presentation.compose.team.member

import com.bottari.presentation.model.bottari.team.member.TeamMemberStatusUiModel

data class ComposeTeamMembersStatusUiState(
    val isLoading: Boolean = false,
    val membersStatus: List<TeamMemberStatusUiModel> = emptyList(),
    val myId: Long = -1L,
    val selectedMember: TeamMemberStatusUiModel? = null,
){
    val checkedMembers: List<TeamMemberStatusUiModel> = membersStatus.filter { it.isAllChecked }
    val uncheckedMembers: List<TeamMemberStatusUiModel> = membersStatus.filter { it.isAllChecked.not() }
}
