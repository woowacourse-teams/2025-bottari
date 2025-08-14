package com.bottari.presentation.view.checklist.team.checklist

import com.bottari.presentation.model.TeamChecklistItemUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel

interface ItemClickListener {
    fun onTypeClick(position: TeamChecklistTypeUiModel)

    fun onItemClick(position: TeamChecklistItemUiModel)
}
