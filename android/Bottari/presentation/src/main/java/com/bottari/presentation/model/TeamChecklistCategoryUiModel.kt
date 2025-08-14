package com.bottari.presentation.model

import com.bottari.presentation.view.checklist.team.checklist.ChecklistCategory

data class TeamChecklistCategoryUiModel(
    val category: ChecklistCategory,
    val teamChecklistItems: List<TeamChecklistItemUiModel>,
    var isExpanded: Boolean = true,
) : TeamChecklistRowUiModel
