package com.bottari.domain.model.team

data class TeamBottariStatus(
    val sharedItems: List<TeamBottariProductStatus>,
    val assignedItems: List<TeamBottariProductStatus>,
)
