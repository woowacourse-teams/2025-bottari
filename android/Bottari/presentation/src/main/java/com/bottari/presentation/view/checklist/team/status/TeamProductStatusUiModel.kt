package com.bottari.presentation.view.checklist.team.status

import android.os.Parcelable
import com.bottari.presentation.view.checklist.team.checklist.ChecklistType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamChecklistUiModel(
    val sharedItems: List<TeamProductStatusUiModel>,
    val assignedItems: List<TeamProductStatusUiModel>,
) : Parcelable

sealed interface TeamProductStatusItem

@Parcelize
data class TeamProductStatusUiModel(
    val name: String,
    val memberCheckStatus: List<MemberCheckStatusUiModel>,
    val checkItemsCount: Int,
    val totalItemsCount: Int,
) : Parcelable,
    TeamProductStatusItem

@Parcelize
data class MemberCheckStatusUiModel(
    val name: String,
    val checked: Boolean,
) : Parcelable

data class TeamChecklistTypeUiModel(
    val type: ChecklistType,
) : TeamProductStatusItem
