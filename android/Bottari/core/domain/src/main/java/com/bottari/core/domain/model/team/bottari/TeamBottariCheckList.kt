package com.bottari.core.domain.model.team.bottari

import com.bottari.core.domain.model.bottari.item.ChecklistItem

data class TeamBottariCheckList(
    val sharedItems: List<ChecklistItem>,
    val assignedItems: List<ChecklistItem>,
    val personalItems: List<ChecklistItem>,
)
