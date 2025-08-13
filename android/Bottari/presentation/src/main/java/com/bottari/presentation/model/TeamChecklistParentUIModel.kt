package com.bottari.presentation.model

import com.bottari.presentation.view.team.checklist.checklist.ChecklistCategory

data class TeamChecklistParentUIModel(
    val category: ChecklistCategory,
    val children: List<TeamBottariItemUiModel>,
    var isExpanded: Boolean = true,
)
