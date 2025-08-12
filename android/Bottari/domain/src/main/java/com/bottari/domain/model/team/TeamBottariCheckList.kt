package com.bottari.domain.model.team

data class TeamBottariCheckList(
    val sharedItems: List<TeamMemberItem>,
    val assignedItems: List<TeamMemberItem>,
    val personalItems: List<TeamMemberItem>,
)
