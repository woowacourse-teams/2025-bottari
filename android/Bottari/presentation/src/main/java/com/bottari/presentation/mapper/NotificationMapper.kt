package com.bottari.presentation.mapper

import com.bottari.domain.model.notification.Notification
import com.bottari.presentation.mapper.AlarmMapper.toDomain
import com.bottari.presentation.mapper.AlarmMapper.toUiModel
import com.bottari.presentation.model.NotificationUiModel

object NotificationMapper {
    fun Notification.toUiModel(): NotificationUiModel =
        NotificationUiModel(
            id = bottariId,
            title = bottariTitle,
            alarm = alarm.toUiModel(),
        )

    fun NotificationUiModel.toDomain(): Notification =
        Notification(
            bottariId = id,
            bottariTitle = title,
            alarm = alarm.toDomain(),
        )
}
