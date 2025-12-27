package com.bottari.presentation.model.bottari.team.member

import androidx.compose.runtime.Immutable
import com.bottari.core.domain.model.team.member.TeamMemberStatus
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

@Immutable
data class TeamMemberStatusUiModel(
    val member: TeamMemberUiModel,
    val totalItemsCount: Int,
    val checkedItemsCount: Int,
    val sharedItems: List<PersonalChecklistItemUiModel>,
    val assignedItems: List<PersonalChecklistItemUiModel>,
    val isMe: Boolean,
    val isExpanded: Boolean = false,
) {
    val unCheckedItems =
        assignedItems.filter { it.isChecked.not() } + sharedItems.filter { it.isChecked.not() }

    val checkedItems = assignedItems.filter { it.isChecked } + sharedItems.filter { it.isChecked }

    val isAllChecked: Boolean = checkedItemsCount == totalItemsCount

    val checkedProgress: Int =
        if (totalItemsCount == 0) {
            0
        } else {
            (((checkedItemsCount.toFloat() / totalItemsCount.toFloat()) * 100).toInt())
                .coerceIn(0, 100)
        }
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
                        PersonalChecklistItemUiModel.fromDomain(
                            sharedItem,
                        )
                    },
                assignedItems =
                    teamMemberStatus.assignedItems.map { assignedItem ->
                        PersonalChecklistItemUiModel.fromDomain(
                            assignedItem,
                        )
                    },
                isMe = teamMemberStatus.id == myId,
            )
    }
}
