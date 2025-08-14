package com.bottari.presentation.view.checklist.team.checklist

interface TeamChecklistItemClickListener {
    fun onTypeClick(type: ChecklistType)

    fun onItemClick(
        id: Long,
        type: ChecklistType,
    )
}
