package com.bottari.domain.model.team.bottari

data class TeamBottariStatus(
    val sharedItems: List<TeamBottariProductStatus>,
    val assignedItems: List<TeamBottariProductStatus>,
)
