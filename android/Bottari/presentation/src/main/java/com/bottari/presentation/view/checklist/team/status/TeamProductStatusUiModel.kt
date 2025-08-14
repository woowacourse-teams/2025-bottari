package com.bottari.presentation.view.checklist.team.status

import com.bottari.presentation.model.BottariItemTypeUiModel

data class TeamBottariStatusUiModel(
    val sharedItems: List<TeamProductStatusUiModel>,
    val assignedItems: List<TeamProductStatusUiModel>,
)

sealed interface TeamProductStatusItem

data class TeamProductStatusUiModel(
    val id: Long,
    val name: String,
    val memberCheckStatus: List<MemberCheckStatusUiModel>,
    val checkItemsCount: Int,
    val totalItemsCount: Int,
    val type: BottariItemTypeUiModel,
) : TeamProductStatusItem

data class MemberCheckStatusUiModel(
    val name: String,
    val checked: Boolean,
)

data class TeamChecklistTypeUiModel(
    val type: BottariItemTypeUiModel,
) : TeamProductStatusItem
