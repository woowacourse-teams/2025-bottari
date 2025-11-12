package com.bottari.presentation.model.bottari.personal

import android.os.Parcelable
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.personal.PersonalBottari
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.bottari.BottariItemUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottariDetailUiModel(
    val id: Long,
    val title: String,
    val alarm: AlarmUiModel?,
    val items: List<BottariItemUiModel> = emptyList(),
) : Parcelable {
    companion object {
        fun fromDomain(bottari: Bottari): BottariDetailUiModel =
            BottariDetailUiModel(
                id = bottari.id,
                title = bottari.title,
                alarm = bottari.alarm?.let { AlarmUiModel.fromDomain(it) },
                items = bottari.items.map { item -> BottariItemUiModel.fromDomain(item) },
            )

        fun fromDomain(bottari: PersonalBottari): BottariDetailUiModel =
            BottariDetailUiModel(
                id = bottari.id,
                title = bottari.title,
                alarm = bottari.alarm?.let { AlarmUiModel.fromDomain(it) },
                items = bottari.items.map { item -> BottariItemUiModel.fromDomain(item) },
            )
    }
}
