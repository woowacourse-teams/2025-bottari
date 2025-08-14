package com.bottari.presentation.view.checklist.team.checklist

import com.bottari.presentation.model.TeamChecklistItem
import com.bottari.presentation.model.TeamChecklistProductUiModel

data class TeamChecklistUiState(
    val isLoading: Boolean = false,
    val items: List<TeamChecklistProductUiModel> = emptyList(),
    val expandableItems: List<TeamChecklistItem> = emptyList(),
)
