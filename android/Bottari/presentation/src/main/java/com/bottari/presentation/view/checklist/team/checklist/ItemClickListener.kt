package com.bottari.presentation.view.checklist.team.checklist

interface ItemClickListener {
    fun onTypeClick(type: ChecklistType)

    fun onItemClick(
        id: Long,
        type: ChecklistType,
    )
}
