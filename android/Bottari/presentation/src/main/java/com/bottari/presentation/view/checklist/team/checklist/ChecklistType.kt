package com.bottari.presentation.view.checklist.team.checklist

import androidx.annotation.StringRes
import com.bottari.presentation.R

enum class ChecklistType(
    @StringRes val title: Int,
) {
    SHARED(R.string.team_checklist_type_shared),
    ASSIGNED(R.string.team_checklist_type_assigned),
    PERSONAL(R.string.team_checklist_type_personal),
}
