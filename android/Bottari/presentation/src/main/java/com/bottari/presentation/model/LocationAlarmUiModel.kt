package com.bottari.presentation.model

import android.os.Parcelable
import com.bottari.domain.model.alarm.LocationAlarm
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationAlarmUiModel(
    val latitude: Double,
    val longitude: Double,
    val radius: Int,
    val isActive: Boolean,
) : Parcelable {
    fun toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    companion object {
        fun fromDomain(locationAlarm: LocationAlarm): LocationAlarmUiModel =
            LocationAlarmUiModel(
                latitude = locationAlarm.latitude,
                longitude = locationAlarm.longitude,
                radius = locationAlarm.radius,
                isActive = locationAlarm.isActive,
            )
    }
}
