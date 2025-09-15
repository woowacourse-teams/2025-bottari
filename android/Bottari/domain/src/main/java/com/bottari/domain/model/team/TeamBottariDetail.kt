package com.bottari.domain.model.team

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariItem

data class TeamBottariDetail(
    val bottari: Bottari,
    val personalItems: List<BottariItem>,
    val assignedItems: List<BottariItem>,
    val sharedItems: List<BottariItem>,
)
