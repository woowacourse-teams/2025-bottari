package com.bottari.presentation.view.checklist.team.main.status

import com.bottari.presentation.model.TeamBottariProductStatusUiModel
import com.bottari.presentation.model.TeamProductStatusItem

data class TeamBottariStatusUiState(
    val isLoading: Boolean = false,
    val sharedItems: List<TeamBottariProductStatusUiModel> = listOf(),
    val assignedItems: List<TeamBottariProductStatusUiModel> = listOf(),
    val selectedProduct: TeamBottariProductStatusUiModel? = null,
    val teamChecklistItems: List<TeamProductStatusItem> = listOf(),
)
