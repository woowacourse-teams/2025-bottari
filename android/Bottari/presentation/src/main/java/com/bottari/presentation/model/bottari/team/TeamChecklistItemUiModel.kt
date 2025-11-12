package com.bottari.presentation.model.bottari.team

import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel

interface ChecklistItemUiModel {
    val id: Long
    val name: String
    val isChecked: Boolean
}

data class TeamChecklistItemUiModel(
    override val id: Long,
    override val name: String,
    override val isChecked: Boolean,
    val type: BottariItemTypeUiModel,
) : ChecklistItemUiModel
