package com.bottari.presentation.mapper

import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.presentation.mapper.AlarmMapper.toUiModel
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
}
