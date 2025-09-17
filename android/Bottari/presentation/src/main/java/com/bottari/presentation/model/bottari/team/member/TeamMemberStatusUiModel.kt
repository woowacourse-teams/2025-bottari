package com.bottari.presentation.model.bottari.team.member

import com.bottari.domain.model.team.member.TeamMemberStatus
import com.bottari.presentation.model.bottari.ChecklistItemUiModel

data class TeamMemberStatusUiModel(
    val member: TeamMemberUiModel,
    val totalItemsCount: Int,
    val checkedItemsCount: Int,
    val sharedItems: List<ChecklistItemUiModel>,
    val assignedItems: List<ChecklistItemUiModel>,
    val isMe: Boolean,
    val isExpanded: Boolean = false,
) {
    private val isAllChecked: Boolean = checkedItemsCount == totalItemsCount

    val isItemsEmpty: Boolean = sharedItems.isEmpty() && assignedItems.isEmpty()

    val shouldHurryUp: Boolean = (isAllChecked || isMe || isItemsEmpty).not()

    companion object {
        fun fromDomain(
            teamMemberStatus: TeamMemberStatus,
            myId: Long,
        ): TeamMemberStatusUiModel =
            TeamMemberStatusUiModel(
                member = TeamMemberUiModel.fromDomain(teamMemberStatus),
                totalItemsCount = teamMemberStatus.itemCount.totalQuantity,
                checkedItemsCount = teamMemberStatus.itemCount.checkedQuantity,
                sharedItems =
                    teamMemberStatus.sharedItems.map { sharedItem ->
                        ChecklistItemUiModel.fromDomain(
                            sharedItem,
                        )
                    },
                assignedItems =
                    teamMemberStatus.assignedItems.map { assignedItem ->
                        ChecklistItemUiModel.fromDomain(
                            assignedItem,
                        )
                    },
                isMe = teamMemberStatus.id == myId,
            )
    }
}
