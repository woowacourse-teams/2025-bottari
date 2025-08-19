package com.bottari.presentation.view.edit.team.item.assigned

import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.TeamMemberUiModel

data class TeamAssignedItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val hasRestoreState: Boolean = false,
    val inputText: String = "",
    val assignedItems: List<BottariItemUiModel> = emptyList(),
    val members: List<TeamMemberUiModel> = emptyList(),
) {
    val isEmpty: Boolean = isFetched && assignedItems.isEmpty()
    val isEditing: Boolean = assignedItems.any { it.isSelected }
    val isAlreadyExist: Boolean =
        isEditing.not() && hasRestoreState.not() && assignedItems.any { it.name == inputText }

    val selectedAssignedItem: BottariItemUiModel? = assignedItems.find { it.isSelected }
    val selectedMemberIds: List<Long> = members.filter { it.isHost }.mapNotNull { it.id }
    val canSend: Boolean = selectedMemberIds.isNotEmpty()
}
