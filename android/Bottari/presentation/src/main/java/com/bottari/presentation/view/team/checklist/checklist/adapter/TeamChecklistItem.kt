package com.bottari.presentation.view.team.checklist.checklist.adapter

import com.bottari.presentation.model.TeamBottariItemUiModel
import com.bottari.presentation.model.TeamChecklistParentUIModel

sealed class TeamChecklistItem {
    data class Parent(val teamChecklistParent: TeamChecklistParentUIModel) : TeamChecklistItem()
    data class Child(val teamBottariItem: TeamBottariItemUiModel) : TeamChecklistItem()
}
