package com.bottari.presentation.model

sealed interface TeamProductStatusItem

data class TeamBottariProductStatusUiModel(
    val id: Long,
    val name: String,
    val memberCheckStatus: List<MemberCheckStatusUiModel>,
    val checkItemsCount: Int,
    val totalItemsCount: Int,
    val type: BottariItemTypeUiModel,
) : TeamProductStatusItem {
    val isAllChecked: Boolean =
        memberCheckStatus.isNotEmpty() && memberCheckStatus.all { it.checked }
}

data class TeamChecklistTypeUiModel(
    val type: BottariItemTypeUiModel,
    val hasItems: Boolean,
) : TeamProductStatusItem
