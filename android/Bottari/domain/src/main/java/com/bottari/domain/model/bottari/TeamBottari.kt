package com.bottari.domain.model.bottari

data class TeamBottari(
    val base: BottariBase,
    val totalQuantity: Int,
    val checkedQuantity: Int,
    val memberCount: Int,
)
