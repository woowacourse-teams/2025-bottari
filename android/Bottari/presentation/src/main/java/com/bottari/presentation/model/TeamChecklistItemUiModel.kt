package com.bottari.presentation.model

import android.os.Parcelable
import com.bottari.presentation.view.checklist.team.checklist.ChecklistType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamChecklistItemUiModel(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
    val type: ChecklistType,
) : Parcelable,
    TeamChecklistRowUiModel
