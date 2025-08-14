package com.bottari.domain.model.team

data class TeamBottariStatus(
    val sharedItems: List<TeamProductStatus>,
    val assignedItems: List<TeamProductStatus>,
)
