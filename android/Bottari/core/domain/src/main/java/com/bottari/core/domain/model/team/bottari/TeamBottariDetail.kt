package com.bottari.core.domain.model.team.bottari

import com.bottari.core.domain.model.bottari.Bottari
import com.bottari.core.domain.model.bottari.item.BottariItem

data class TeamBottariDetail(
    val bottari: Bottari,
    val personalItems: List<BottariItem>,
    val assignedItems: List<BottariItem>,
    val sharedItems: List<BottariItem>,
)
