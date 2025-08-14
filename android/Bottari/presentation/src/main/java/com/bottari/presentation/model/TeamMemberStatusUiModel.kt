package com.bottari.presentation.model

data class TeamMemberStatusUiModel(
    val member: TeamMemberUiModel,
    val totalItemsCount: Int,
    val checkedItemsCount: Int,
    val sharedItems: List<ChecklistItemUiModel>,
    val assignedItems: List<ChecklistItemUiModel>,
) {
    val isItemsEmpty: Boolean
        get() = sharedItems.isEmpty() && assignedItems.isEmpty()
}
