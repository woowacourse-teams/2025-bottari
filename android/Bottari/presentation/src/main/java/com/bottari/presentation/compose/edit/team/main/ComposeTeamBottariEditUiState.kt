package com.bottari.presentation.compose.edit.team.main

import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.bottari.BottariItemUiModel

data class ComposeTeamBottariEditUiState(
    val isLoading: Boolean = false,
    val bottariTitle: String = "",
    val personalItems: List<BottariItemUiModel> = emptyList(),
    val sharedItems: List<BottariItemUiModel> = emptyList(),
    val assignedItems: List<BottariItemUiModel> = emptyList(),
    val alarm: AlarmUiModel? = null,
    val alarmSwitchState: Boolean = false,
    val isFetched: Boolean = false,
) {
    val isPersonalItemsEmpty: Boolean = isFetched && personalItems.isEmpty()
    val isSharedItemsEmpty: Boolean = isFetched && sharedItems.isEmpty()
    val isAssignedItemsEmpty: Boolean = isFetched && assignedItems.isEmpty()
}
