package com.bottari.core.ui.model.bottari.team

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.bottari.personal.BottariItemTypeUiModel

@Immutable
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
