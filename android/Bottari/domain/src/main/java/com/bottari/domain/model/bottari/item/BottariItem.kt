package com.bottari.domain.model.bottari.item

import com.bottari.domain.model.team.bottari.item.TeamBottariItemType

data class BottariItem(
    val id: Long,
    val name: String,
    val type: TeamBottariItemType,
)
