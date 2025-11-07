package com.bottari.presentation.compose.edit.team.assigned

import com.bottari.presentation.model.bottari.personal.SelectableItemUiModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel

data class TeamAssignedEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val hasRestoreState: Boolean = false,
    val inputText: String = "",
    val assignedItems: List<SelectableItemUiModel> = emptyList(),
    val members: List<TeamMemberUiModel> = emptyList(),
) {
    val isEmpty: Boolean = isFetched && assignedItems.isEmpty()
    val isEditing: Boolean = assignedItems.any { it.isSelected }
    val isAlreadyExist: Boolean =
        isEditing.not() && hasRestoreState.not() && assignedItems.any { it.name == inputText }

    val selectedAssignedItem: SelectableItemUiModel? = assignedItems.find { it.isSelected }
    val selectedMemberIds: List<Long> = members.filter { it.isHost }.mapNotNull { it.id }

    val isInputTextBlank = inputText.isBlank()

    val canSend: Boolean = selectedMemberIds.isNotEmpty() && !isAlreadyExist && !isInputTextBlank
}
