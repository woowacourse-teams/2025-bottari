package com.bottari.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface TeamChecklistItem

@Parcelize
data class TeamChecklistProductUiModel(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
    val type: BottariItemTypeUiModel,
) : Parcelable,
    TeamChecklistItem

data class TeamChecklistExpendableTypeUiModel(
    val type: BottariItemTypeUiModel,
    val teamChecklistItems: List<TeamChecklistProductUiModel>,
    var isExpanded: Boolean = true,
) : TeamChecklistItem
