package com.bottari.presentation.model

sealed interface TeamProductStatusItem

data class TeamProductStatusUiModel(
    val id: Long,
    val name: String,
    val memberCheckStatus: List<MemberCheckStatusUiModel>,
    val checkItemsCount: Int,
    val totalItemsCount: Int,
    val type: BottariItemTypeUiModel,
) : TeamProductStatusItem

data class TeamChecklistTypeUiModel(
    val type: BottariItemTypeUiModel,
) : TeamProductStatusItem
