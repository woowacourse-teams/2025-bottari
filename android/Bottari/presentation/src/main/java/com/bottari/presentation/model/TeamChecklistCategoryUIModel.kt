package com.bottari.presentation.model

import com.bottari.presentation.view.team.checklist.checklist.ChecklistCategory

data class TeamChecklistCategoryUIModel(
    val category: ChecklistCategory,
    val teamChecklistItems: List<TeamChecklistItemUIModel>,
    var isExpanded: Boolean = true,
) : TeamChecklistRowUiModel
