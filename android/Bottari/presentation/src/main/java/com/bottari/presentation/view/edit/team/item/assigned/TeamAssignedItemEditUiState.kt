package com.bottari.presentation.view.edit.team.item.assigned

import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.TeamMemberUiModel

data class TeamAssignedItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val assignedItems: List<BottariItemUiModel> = emptyList(),
    val members: List<TeamMemberUiModel> = emptyList(),
    val inputText: String = "",
) {
    val isEmpty: Boolean = isFetched && assignedItems.isEmpty()
    val matchingItem: BottariItemUiModel? = assignedItems.find { it.name == inputText }
    val selectedMemberIds: List<Long> =
        members
            .filter { member -> member.isHost }
            .mapNotNull { member -> member.id }
    val sendCondition: Boolean = selectedMemberIds.isNotEmpty()
}
