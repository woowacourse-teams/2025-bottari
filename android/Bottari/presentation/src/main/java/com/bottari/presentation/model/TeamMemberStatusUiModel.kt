package com.bottari.presentation.model

data class TeamMemberStatusUiModel(
    val member: TeamMemberUiModel,
    val totalItemsCount: Int,
    val checkedItemsCount: Int,
    val sharedItems: List<BottariItemUiModel>,
    val assignedItems: List<BottariItemUiModel>,
)
