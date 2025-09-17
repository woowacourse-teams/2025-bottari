package com.bottari.presentation.model.bottari.team

import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel

sealed interface TeamChecklistItem

data class TeamChecklistProductUiModel(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
    val type: BottariItemTypeUiModel,
) : TeamChecklistItem

data class TeamChecklistExpandableTypeUiModel(
    val type: BottariItemTypeUiModel,
    val teamChecklistItems: List<TeamChecklistProductUiModel>,
    var isExpanded: Boolean = true,
) : TeamChecklistItem
