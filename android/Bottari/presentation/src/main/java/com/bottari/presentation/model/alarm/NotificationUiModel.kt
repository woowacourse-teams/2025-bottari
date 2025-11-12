package com.bottari.presentation.model.alarm

import android.os.Parcelable
import com.bottari.domain.model.notification.Notification
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationUiModel(
    val bottariId: Long,
    val bottariTitle: String,
    val alarm: AlarmUiModel,
) : Parcelable {
    fun toDomain(): Notification =
        Notification(
            bottariId = bottariId,
            bottariTitle = bottariTitle,
            alarm = alarm.toDomain(),
        )

    companion object {
        fun fromDomain(notification: Notification): NotificationUiModel =
            NotificationUiModel(
                bottariId = notification.bottariId,
                bottariTitle = notification.bottariTitle,
                alarm = AlarmUiModel.fromDomain(notification.alarm),
            )
    }
}
