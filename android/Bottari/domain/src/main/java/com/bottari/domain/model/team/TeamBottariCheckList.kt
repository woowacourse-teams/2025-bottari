package com.bottari.domain.model.team

import com.bottari.domain.model.bottari.ChecklistItem

data class TeamBottariCheckList(
    val sharedItems: List<ChecklistItem>,
    val assignedItems: List<ChecklistItem>,
    val personalItems: List<ChecklistItem>,
)
