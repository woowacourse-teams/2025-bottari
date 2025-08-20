package com.bottari.presentation.model

data class TeamMemberStatusUiModel(
    val member: TeamMemberUiModel,
    val totalItemsCount: Int,
    val checkedItemsCount: Int,
    val sharedItems: List<ChecklistItemUiModel>,
    val assignedItems: List<ChecklistItemUiModel>,
    val isMe: Boolean,
) {
    val isItemsEmpty: Boolean
        get() = sharedItems.isEmpty() && assignedItems.isEmpty()
    val isAllChecked: Boolean = checkedItemsCount == totalItemsCount
}
