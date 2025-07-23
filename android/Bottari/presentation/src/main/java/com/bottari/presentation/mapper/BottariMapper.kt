package com.bottari.presentation.mapper

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.presentation.mapper.AlarmMapper.toDomain
import com.bottari.presentation.mapper.AlarmMapper.toUiModel
import com.bottari.presentation.model.BottariDetailUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.BottariUiModel

object BottariMapper {
    fun Bottari.toUiModel(): BottariUiModel =
        BottariUiModel(
            id = id,
            title = title,
            totalQuantity = totalQuantity,
            checkedQuantity = checkedQuantity,
            alarm = alarm?.toUiModel(),
        )

    fun BottariUiModel.toDomain(): Bottari =
        Bottari(
            id = id,
            title = title,
            totalQuantity = totalQuantity,
            checkedQuantity = checkedQuantity,
            alarm = alarm?.toDomain(),
        )

    fun BottariDetail.toUiModel(): BottariDetailUiModel =
        BottariDetailUiModel(
            id = id,
            title = title,
            alarm = alarm?.toUiModel(),
            items = items.map { item -> item.toUiModel() },
        )

    fun BottariDetailUiModel.toDomain(): BottariDetail =
        BottariDetail(
            id = id,
            title = title,
            alarm = alarm?.toDomain(),
            items = items.map { item -> item.toDomain() },
        )

    private fun BottariItem.toUiModel(): BottariItemUiModel =
        BottariItemUiModel(
            id = id,
            isChecked = isChecked,
            name = name,
        )

    private fun BottariItemUiModel.toDomain(): BottariItem =
        BottariItem(
            id = id,
            isChecked = isChecked,
            name = name,
        )
}
