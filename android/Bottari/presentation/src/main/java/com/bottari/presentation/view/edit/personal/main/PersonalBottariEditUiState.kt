package com.bottari.presentation.view.edit.personal.main

import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.bottari.BottariItemUiModel
import com.bottari.presentation.model.bottari.personal.BottariDetailUiModel

data class PersonalBottariEditUiState(
    val isLoading: Boolean = false,
    val id: Long,
    val title: String = "",
    val alarm: AlarmUiModel? = null,
    val items: List<BottariItemUiModel> = emptyList(),
) {
    val isAlarmActive: Boolean = alarm?.isActive ?: false

    companion object {
        fun from(bottari: BottariDetailUiModel): PersonalBottariEditUiState =
            PersonalBottariEditUiState(
                isLoading = false,
                id = bottari.id,
                title = bottari.title,
                alarm = bottari.alarm,
                items = bottari.items,
            )
    }
}
