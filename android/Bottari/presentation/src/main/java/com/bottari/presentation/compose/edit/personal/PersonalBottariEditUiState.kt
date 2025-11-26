package com.bottari.presentation.compose.edit.personal

import com.bottari.core.domain.model.bottari.personal.PersonalBottari
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
