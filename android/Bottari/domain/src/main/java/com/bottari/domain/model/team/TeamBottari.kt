package com.bottari.domain.model.team

import com.bottari.domain.model.bottari.Bottari

data class TeamBottari(
    val bottari: Bottari,
    val totalQuantity: Int,
    val checkedQuantity: Int,
    val memberCount: HeadCount,
)
