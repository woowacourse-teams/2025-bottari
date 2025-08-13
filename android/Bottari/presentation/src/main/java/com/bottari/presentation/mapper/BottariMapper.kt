package com.bottari.presentation.mapper

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.ChecklistItem
import com.bottari.presentation.mapper.AlarmMapper.toUiModel
import com.bottari.presentation.model.BottariDetailUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.BottariUiModel
import com.bottari.presentation.model.ChecklistItemUiModel
import com.bottari.presentation.model.MyBottariUiModel

object BottariMapper {
    fun Bottari.toUiModel(): BottariUiModel =
        BottariUiModel(
            id = id,
            title = title,
            totalQuantity = totalQuantity,
            checkedQuantity = checkedQuantity,
            alarm = alarm?.toUiModel(),
        )

    fun BottariDetail.toUiModel(): BottariDetailUiModel =
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
            type = type,
        )

    fun BottariDetail.toMyBottariUiModel(): MyBottariUiModel =
        MyBottariUiModel(
            id = id,
            title = title,
            isSelected = false,
            items = items.map { item -> item.toUiModel() },
        )
}
