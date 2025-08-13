package com.bottari.presentation.view.team.checklist.checklist.adapter

import com.bottari.presentation.model.TeamBottariItemUIModel
import com.bottari.presentation.model.TeamChecklistParentUIModel

sealed class TeamChecklistItem {
    data class CategoryPage(
        val teamChecklistParent: TeamChecklistParentUIModel,
    ) : TeamChecklistItem()

    data class TeamBottariItem(
        val teamBottariItem: TeamBottariItemUIModel,
    ) : TeamChecklistItem()
}
