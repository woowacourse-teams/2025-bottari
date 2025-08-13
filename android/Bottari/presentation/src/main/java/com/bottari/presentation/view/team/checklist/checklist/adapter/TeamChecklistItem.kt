package com.bottari.presentation.view.team.checklist.checklist.adapter

import com.bottari.presentation.model.TeamChecklistCategoryUIModel
import com.bottari.presentation.model.TeamChecklistItemUIModel

sealed class TeamChecklistItem {
    data class Category(
        val teamChecklistCategory: TeamChecklistCategoryUIModel,
    ) : TeamChecklistItem()

    data class Item(
        val teamBottariItem: TeamChecklistItemUIModel,
    ) : TeamChecklistItem()
}
