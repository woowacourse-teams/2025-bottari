package com.bottari.presentation.mapper

import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.TeamMemberItem
import com.bottari.presentation.mapper.AlarmMapper.toUiModel
import com.bottari.presentation.model.TeamBottariItemUIModel
import com.bottari.presentation.model.TeamBottariUiModel
import com.bottari.presentation.view.team.checklist.checklist.ChecklistCategory

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

    fun TeamMemberItem.toUiModel(category: ChecklistCategory): TeamBottariItemUIModel =
        TeamBottariItemUIModel(
            id = id,
            name = name,
            isChecked = isChecked,
            category = category,
        )
}
