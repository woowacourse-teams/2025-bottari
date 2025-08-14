package com.bottari.presentation.model

import android.os.Parcelable
import com.bottari.presentation.view.checklist.team.checklist.ChecklistCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamChecklistItemUiModel(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
    val category: ChecklistCategory,
) : Parcelable,
    TeamChecklistRowUiModel
