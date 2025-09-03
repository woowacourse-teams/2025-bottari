package com.bottari.presentation.view.edit.personal.main

import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.bottari.BottariItemUiModel

data class PersonalBottariEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val bottariId: Long,
    val bottariTitle: String = "",
    val alarm: AlarmUiModel? = null,
    val items: List<BottariItemUiModel> = emptyList(),
    val isAlarmActive: Boolean = false,
) {
    val isEmpty: Boolean = isFetched && items.isEmpty()
    val isShowAlarm: Boolean = isAlarmActive && alarm != null
    val isShowAlarmCreate: Boolean = isAlarmActive && alarm == null
}
