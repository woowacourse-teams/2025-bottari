package com.bottari.presentation.model

import com.bottari.presentation.view.checklist.team.checklist.ChecklistType

data class TeamChecklistTypeUiModel(
    val type: ChecklistType,
    val teamChecklistItems: List<TeamChecklistProductUiModel>,
    var isExpanded: Boolean = true,
) : TeamChecklistItem
