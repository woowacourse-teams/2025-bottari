package com.bottari.presentation.view.checklist.team.checklist

import com.bottari.presentation.model.TeamChecklistProductUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel

interface ItemClickListener {
    fun onTypeClick(position: TeamChecklistTypeUiModel)

    fun onItemClick(position: TeamChecklistProductUiModel)
}
