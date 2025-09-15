package com.bottari.presentation.mapper

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariState
import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.presentation.mapper.AlarmMapper.toUiModel
import com.bottari.presentation.mapper.TeamMembersMapper.toUiModel
import com.bottari.presentation.model.BottariDetailUiModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.BottariUiModel
import com.bottari.presentation.model.ChecklistItemUiModel
import com.bottari.presentation.model.MyBottariUiModel

object BottariMapper {
    fun BottariState.toUiModel(): BottariUiModel =
        BottariUiModel(
            id = bottari.id,
            title = bottari.title,
            totalQuantity = itemCount.totalQuantity,
            checkedQuantity = itemCount.checkedQuantity,
            alarm = bottari.alarm?.toUiModel(),
        )

    fun Bottari.toUiModel(): BottariDetailUiModel =
        BottariDetailUiModel(
            id = id,
            title = title,
            alarm = alarm?.toUiModel(),
            items = items.map { item -> item.toUiModel() },
        )

    fun ChecklistItem.toUiModel(): ChecklistItemUiModel =
        ChecklistItemUiModel(
            id = id,
            isChecked = isChecked,
            name = name,
        )

    fun BottariItem.toUiModel(): BottariItemUiModel =
        BottariItemUiModel(
            id = id,
            name = name,
            type = type.toUiModel(),
        )

    fun Bottari.toMyBottariUiModel(): MyBottariUiModel =
        MyBottariUiModel(
            id = id,
            title = title,
            isSelected = false,
            items = items.map { item -> item.toUiModel() },
        )

    fun TeamBottariItemType.toUiModel(): BottariItemTypeUiModel =
        when (this) {
            TeamBottariItemType.PERSONAL -> BottariItemTypeUiModel.PERSONAL
            TeamBottariItemType.SHARED -> BottariItemTypeUiModel.SHARED
            is TeamBottariItemType.ASSIGNED -> BottariItemTypeUiModel.ASSIGNED(members.map { member -> member.toUiModel() })
        }
}
