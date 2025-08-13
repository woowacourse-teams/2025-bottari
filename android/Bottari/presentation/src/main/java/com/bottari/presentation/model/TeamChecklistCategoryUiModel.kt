package com.bottari.presentation.model

import com.bottari.presentation.view.team.checklist.checklist.ChecklistCategory

data class TeamChecklistCategoryUiModel(
    val category: ChecklistCategory,
    val teamChecklistItems: List<TeamChecklistItemUiModel>,
    var isExpanded: Boolean = true,
) : TeamChecklistRowUiModel
