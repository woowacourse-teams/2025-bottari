package com.bottari.core.ui.model.bottari.personal

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.bottari.core.domain.model.bottari.Bottari
import com.bottari.core.domain.model.bottari.personal.PersonalBottari
import com.bottari.core.ui.model.alarm.AlarmUiModel
import com.bottari.core.ui.model.bottari.BottariItemUiModel
import kotlinx.parcelize.Parcelize

@Immutable
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
