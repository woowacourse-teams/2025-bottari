package com.bottari.presentation.view.edit.personal.main

import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.BottariDetailUiModel
import com.bottari.presentation.model.BottariItemUiModel

data class PersonalBottariEditUiState(
    val isLoading: Boolean = false,
    val id: Long,
    val title: String = "",
    val alarm: AlarmUiModel? = null,
    val items: List<BottariItemUiModel> = emptyList(),
) {
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
