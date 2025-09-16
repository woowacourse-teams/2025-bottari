package com.bottari.presentation.model

import com.bottari.domain.model.team.member.TeamMemberStatus

data class TeamMemberStatusUiModel(
    val member: TeamMemberUiModel,
    val totalItemsCount: Int,
    val checkedItemsCount: Int,
    val sharedItems: List<ChecklistItemUiModel>,
    val assignedItems: List<ChecklistItemUiModel>,
    val isMe: Boolean,
    val isExpanded: Boolean = false,
) {
    val isItemsEmpty: Boolean
        get() = sharedItems.isEmpty() && assignedItems.isEmpty()
    val isAllChecked: Boolean = checkedItemsCount == totalItemsCount

    val shouldHurryUp: Boolean
        get() = (isAllChecked || isMe || isItemsEmpty).not()

    companion object {
        fun fromDomain(
            teamMemberStatus: TeamMemberStatus,
            myId: Long,
        ): TeamMemberStatusUiModel =
            TeamMemberStatusUiModel(
                member = TeamMemberUiModel(teamMemberStatus.id, teamMemberStatus.nickname.value, teamMemberStatus.isHost),
                totalItemsCount = teamMemberStatus.itemCount.totalQuantity,
                checkedItemsCount = teamMemberStatus.itemCount.checkedQuantity,
                sharedItems = teamMemberStatus.sharedItems.map { sharedItem -> ChecklistItemUiModel.fromDomain(sharedItem) },
                assignedItems = teamMemberStatus.assignedItems.map { assignedItem -> ChecklistItemUiModel.fromDomain(assignedItem) },
                isMe = teamMemberStatus.id == myId,
            )
    }
}
