package com.bottari.domain.model.team

import com.bottari.domain.model.bottari.BottariInfo
import com.bottari.domain.model.bottari.BottariItem

data class TeamBottariDetail(
    val info: BottariInfo,
    val personalItems: List<BottariItem>,
    val assignedItems: List<BottariItem>,
    val sharedItems: List<BottariItem>,
)
