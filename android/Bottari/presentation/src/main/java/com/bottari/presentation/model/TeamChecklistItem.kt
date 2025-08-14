package com.bottari.presentation.model

import android.os.Parcelable
import com.bottari.presentation.view.checklist.team.checklist.ChecklistType
import kotlinx.parcelize.Parcelize

sealed interface TeamChecklistItem

@Parcelize
data class TeamChecklistProductUiModel(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
    val type: ChecklistType,
) : Parcelable,
    TeamChecklistItem

data class TeamChecklistTypeUiModel(
    val type: ChecklistType,
    val teamChecklistItems: List<TeamChecklistProductUiModel>,
    var isExpanded: Boolean = true,
) : TeamChecklistItem
