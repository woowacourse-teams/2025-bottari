package com.bottari.domain.model.bottari.personal

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.bottari.item.ChecklistItem

data class PersonalBottari(
    val id: Long,
    val title: String,
    val alarm: Alarm?,
    val items: List<ChecklistItem>,
) {
    val totalQuantity: Int = items.size
    val checkedQuantity: Int = items.count { item -> item.isChecked }
}
