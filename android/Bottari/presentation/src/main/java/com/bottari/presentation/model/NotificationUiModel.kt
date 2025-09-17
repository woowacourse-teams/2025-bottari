package com.bottari.presentation.model

import android.os.Parcelable
import com.bottari.domain.model.notification.Notification
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationUiModel(
    val id: Long,
    val title: String,
    val alarm: AlarmUiModel,
) : Parcelable {
    companion object {
        fun fromDomain(notification: Notification): NotificationUiModel =
            NotificationUiModel(
                id = notification.bottariId,
                title = notification.bottariTitle,
                alarm = AlarmUiModel.fromDomain(notification.alarm),
            )
    }
}
