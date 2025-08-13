package com.bottari.domain.model.team

import com.bottari.domain.model.bottari.BottariItem

data class TeamBottariCheckList(
    val sharedItems: List<BottariItem>,
    val assignedItems: List<BottariItem>,
    val personalItems: List<BottariItem>,
)
