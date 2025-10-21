package com.bottari.presentation.compose.edit.personal

import com.bottari.domain.model.bottari.personal.PersonalBottari
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.bottari.ChecklistItemUiModel

data class PersonalBottariEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val bottariId: Long,
    val bottariTitle: String = "",
    val alarm: AlarmUiModel? = null,
    val items: List<ChecklistItemUiModel> = emptyList(),
    val isAlarmActive: Boolean = false,
) {
    val isEmpty: Boolean = isFetched && items.isEmpty()
    val isShowAlarm: Boolean = isAlarmActive && alarm != null
    val isShowAlarmCreate: Boolean = isAlarmActive && alarm == null

    companion object {
        fun from(bottari: PersonalBottari): PersonalBottariEditUiState =
            PersonalBottariEditUiState(
                isLoading = false,
                isFetched = true,
                bottariId = bottari.id,
                bottariTitle = bottari.title,
                alarm = bottari.alarm?.let(AlarmUiModel::fromDomain),
                items = bottari.items.map(ChecklistItemUiModel::fromDomain),
                isAlarmActive = bottari.alarm?.isActive ?: false,
            )
    }
}
