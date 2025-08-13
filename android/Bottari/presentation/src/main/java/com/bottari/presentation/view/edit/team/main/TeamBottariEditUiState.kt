package com.bottari.presentation.view.edit.team.main

import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.BottariItemUiModel

data class TeamBottariEditUiState(
    val isLoading: Boolean = false,
    val bottariTitle: String = "",
    val personalItems: List<BottariItemUiModel> = emptyList(),
    val sharedItems: List<BottariItemUiModel> = emptyList(),
    val assignedItems: List<BottariItemUiModel> = emptyList(),
    val alarm: AlarmUiModel? = null,
    val isFetched: Boolean = false,
) {
    val isPersonalItemsEmpty: Boolean = isFetched && personalItems.isEmpty()
    val isSharedItemsEmpty: Boolean = isFetched && sharedItems.isEmpty()
    val isAssignedItemsEmpty: Boolean = isFetched && assignedItems.isEmpty()
}
