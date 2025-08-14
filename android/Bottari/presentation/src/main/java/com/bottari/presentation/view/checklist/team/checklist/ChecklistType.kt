package com.bottari.presentation.view.checklist.team.checklist

import androidx.annotation.StringRes
import com.bottari.presentation.R

enum class ChecklistType {
    SHARED,
    ASSIGNED,
    PERSONAL,
}

@StringRes
fun ChecklistType.getStringResId(): Int =
    when (this) {
        ChecklistType.SHARED -> R.string.team_checklist_type_shared_text
        ChecklistType.ASSIGNED -> R.string.team_checklist_type_assigned_text
        ChecklistType.PERSONAL -> R.string.team_checklist_type_personal_text
    }
