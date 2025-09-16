package com.bottari.presentation.model

import com.bottari.domain.model.team.bottari.TeamBottariProductStatus

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

    companion object {
        fun fromDomain(
            teamBottariProductStatus: TeamBottariProductStatus,
            type: BottariItemTypeUiModel,
        ): TeamBottariProductStatusUiModel =
            TeamBottariProductStatusUiModel(
                id = teamBottariProductStatus.id,
                name = teamBottariProductStatus.name,
                memberCheckStatus = teamBottariProductStatus.memberCheckStatus.map { item -> MemberCheckStatusUiModel.fromDomain(item) },
                checkItemsCount = teamBottariProductStatus.itemCount.checkedQuantity,
                totalItemsCount = teamBottariProductStatus.itemCount.totalQuantity,
                type = type,
            )
    }
}

data class TeamChecklistTypeUiModel(
    val type: BottariItemTypeUiModel,
) : TeamProductStatusItem
