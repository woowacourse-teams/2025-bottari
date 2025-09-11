package com.bottari.domain.model.team

import com.bottari.domain.model.bottari.BottariBase
import com.bottari.domain.model.bottari.BottariItem

data class TeamBottariDetail(
    val base: BottariBase,
    val personalItems: List<BottariItem>,
    val assignedItems: List<BottariItem>,
    val sharedItems: List<BottariItem>,
)
