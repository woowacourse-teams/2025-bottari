package com.bottari.presentation.view.edit.personal.main

import com.bottari.domain.model.bottari.personal.PersonalBottari
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

data class PersonalBottariEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val bottariId: Long,
    val bottariTitle: String = "",
    val alarm: AlarmUiModel? = null,
    val items: List<PersonalChecklistItemUiModel> = emptyList(),
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
                items = bottari.items.map(PersonalChecklistItemUiModel::fromDomain),
                isAlarmActive = bottari.alarm?.isActive ?: false,
            )
    }
}
