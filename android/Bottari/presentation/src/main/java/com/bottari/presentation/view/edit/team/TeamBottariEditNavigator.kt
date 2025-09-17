package com.bottari.presentation.view.edit.team

import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel

interface TeamBottariEditNavigator {
    fun navigateBack()

    fun navigateToMemberEdit(teamBottariId: Long)

    fun navigateToItemEdit(
        teamBottariId: Long,
        requireTabType: BottariItemTypeUiModel,
    )
}
