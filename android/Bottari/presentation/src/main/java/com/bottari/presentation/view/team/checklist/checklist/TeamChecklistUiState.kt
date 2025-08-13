package com.bottari.presentation.view.team.checklist.checklist

import com.bottari.presentation.model.TeamChecklistItemUIModel
import com.bottari.presentation.model.TeamChecklistRowUiModel

data class TeamChecklistUiState(
    val isLoading: Boolean = false,
    val items: List<TeamChecklistItemUIModel> = emptyList(),
    val expandableItems: List<TeamChecklistRowUiModel> = emptyList(),
)
