package com.bottari.presentation.mapper

import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.MemberCheckStatus
import com.bottari.domain.model.team.TeamBottariStatus
import com.bottari.domain.model.team.TeamProductStatus
import com.bottari.presentation.mapper.AlarmMapper.toUiModel
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.MemberCheckStatusUiModel
import com.bottari.presentation.model.TeamBottariProductStatusUiModel
import com.bottari.presentation.model.TeamBottariStatusUiModel
import com.bottari.presentation.model.TeamBottariUiModel

object TeamBottariMapper {
    fun TeamBottari.toUiModel(): TeamBottariUiModel =
        TeamBottariUiModel(
            id = id,
            title = title,
            totalQuantity = totalQuantity,
            checkedQuantity = checkedQuantity,
            memberCount = memberCount,
            alarm = alarm?.toUiModel(),
        )

    fun BottariItem.toUiModel(): BottariItemUiModel =
        BottariItemUiModel(
            id = id,
            name = name,
            type = type.toUiModel(),
        )

    fun TeamBottariStatus.toUiModel(): TeamBottariStatusUiModel =
        TeamBottariStatusUiModel(
            sharedItems = sharedItems.map { item -> item.toSharedUiModel() },
            assignedItems = assignedItems.map { item -> item.toAssignedUiModel() },
        )

    private fun TeamProductStatus.toSharedUiModel(): TeamBottariProductStatusUiModel =
        TeamBottariProductStatusUiModel(
            id = id,
            name = name,
            memberCheckStatus = memberCheckStatus.map { item -> item.toUiModel() },
            checkItemsCount = checkItemsCount,
            totalItemsCount = totalItemsCount,
            type = BottariItemTypeUiModel.SHARED,
        )

    private fun TeamProductStatus.toAssignedUiModel(): TeamBottariProductStatusUiModel =
        TeamBottariProductStatusUiModel(
            id = id,
            name = name,
            memberCheckStatus = memberCheckStatus.map { item -> item.toUiModel() },
            checkItemsCount = checkItemsCount,
            totalItemsCount = totalItemsCount,
            type = BottariItemTypeUiModel.ASSIGNED(),
        )

    private fun MemberCheckStatus.toUiModel(): MemberCheckStatusUiModel =
        MemberCheckStatusUiModel(
            name = name,
            checked = checked,
        )
}
