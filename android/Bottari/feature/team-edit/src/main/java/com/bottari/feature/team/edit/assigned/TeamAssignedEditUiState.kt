package com.bottari.feature.team.edit.assigned

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.bottari.personal.SelectableItemUiModel
import com.bottari.core.ui.model.bottari.team.member.TeamMemberUiModel

@Immutable
data class TeamAssignedEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val hasRestoreState: Boolean = false,
    val inputText: String = "",
    val assignedItems: List<SelectableItemUiModel> = emptyList(),
    val members: List<TeamMemberUiModel> = emptyList(),
    val isCreating: Boolean = false,
) {
    val isEmpty: Boolean = isFetched && assignedItems.isEmpty()
    val isEditing: Boolean = assignedItems.any { it.isSelected }

    val isSheetVisible: Boolean = isCreating || isEditing

    val isAlreadyExist: Boolean =
        isEditing.not() && hasRestoreState.not() && assignedItems.any { it.name == inputText }

    val selectedAssignedItem: SelectableItemUiModel? = assignedItems.find { it.isSelected }
    val selectedMemberIds: List<Long> = members.filter { it.isHost }.mapNotNull { it.id }

    val isInputTextBlank = inputText.isBlank()

    val canSend: Boolean = selectedMemberIds.isNotEmpty() && !isAlreadyExist && !isInputTextBlank
}
