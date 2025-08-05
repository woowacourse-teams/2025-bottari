package com.bottari.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationAlarmUiModel(
    val latitude: Double,
    val longitude: Double,
    val radius: Int,
    val isActive: Boolean,
) : Parcelable
