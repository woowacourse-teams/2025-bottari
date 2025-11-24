package com.bottari.core.domain.model.bottari.item

import com.bottari.core.domain.model.team.bottari.item.TeamBottariItemType

data class BottariItem(
    val id: Long,
    val name: String,
    val type: TeamBottariItemType,
)
