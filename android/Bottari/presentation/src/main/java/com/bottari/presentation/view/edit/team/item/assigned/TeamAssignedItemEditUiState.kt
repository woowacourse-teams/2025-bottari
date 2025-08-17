package com.bottari.presentation.view.edit.team.item.assigned

import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.TeamMemberUiModel

data class TeamAssignedItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val assignedItems: List<BottariItemUiModel> = emptyList(),
    val selectedItem: BottariItemUiModel? = null,
    val selectedMembers: List<TeamMemberUiModel> = emptyList(),
    val inputText: String = "",
) {
    val isEmpty: Boolean = isFetched && assignedItems.isEmpty()
    val isAlreadyExist: Boolean = assignedItems.any { it.name == inputText }
    val selectedMemberIds: List<Long> = selectedMembers.mapNotNull { it.id }
}
