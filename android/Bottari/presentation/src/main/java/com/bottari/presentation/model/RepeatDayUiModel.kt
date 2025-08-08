package com.bottari.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek

@Parcelize
data class RepeatDayUiModel(
    val dayOfWeek: DayOfWeek,
    val isChecked: Boolean,
) : Parcelable
