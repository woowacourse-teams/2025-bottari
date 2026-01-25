package com.bottari.feature.mybottari.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek

@Immutable
@Parcelize
data class RepeatDayUiModel(
    val dayOfWeek: DayOfWeek,
    val isChecked: Boolean,
) : Parcelable {
    companion object {
        val DEFAULT_WEEK: List<RepeatDayUiModel> = DayOfWeek.entries.map(::from)

        fun from(dayOfWeek: DayOfWeek): RepeatDayUiModel = RepeatDayUiModel(dayOfWeek = dayOfWeek, isChecked = false)
    }
}
