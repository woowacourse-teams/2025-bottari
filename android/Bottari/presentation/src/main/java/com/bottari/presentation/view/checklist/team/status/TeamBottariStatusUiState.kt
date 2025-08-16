package com.bottari.presentation.view.checklist.team.status

import com.bottari.presentation.model.TeamBottariProductStatusUiModel
import com.bottari.presentation.model.TeamBottariStatusUiModel
import com.bottari.presentation.model.TeamProductStatusItem

data class TeamBottariStatusUiState(
    val isLoading: Boolean = false,
    val teamChecklistStatus: TeamBottariStatusUiModel? = null,
    val selectedProduct: TeamBottariProductStatusUiModel? = null,
    val teamChecklistItems: List<TeamProductStatusItem> = listOf(),
)
