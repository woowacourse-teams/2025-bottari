package com.bottari.presentation.mapper

import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.presentation.mapper.AlarmMapper.toUiModel
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.model.BottariItemUiModel
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
}
