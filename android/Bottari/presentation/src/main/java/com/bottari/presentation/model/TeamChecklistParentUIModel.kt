package com.bottari.presentation.model

import com.bottari.presentation.view.team.checklist.checklist.ChecklistCategory

data class TeamChecklistParentUIModel(
    val category: ChecklistCategory,
    val teamChecklistItems: List<TeamBottariItemUIModel>,
    var isExpanded: Boolean = true,
)
