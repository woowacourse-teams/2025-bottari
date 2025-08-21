package com.bottari.presentation.model

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
}
