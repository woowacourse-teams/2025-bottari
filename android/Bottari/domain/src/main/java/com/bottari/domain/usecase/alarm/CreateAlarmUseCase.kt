package com.bottari.domain.usecase.alarm

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.exception.mapCatching
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.repository.AlarmRepository
import com.bottari.domain.repository.NotificationRepository

class CreateAlarmUseCase(
    private val alarmRepository: AlarmRepository,
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        bottariTitle: String,
        alarm: Alarm,
    ): BottariResult<Unit> =
        alarmRepository
            .createAlarm(bottariId, alarm)
            .mapCatching {
                notificationRepository.saveNotification(
                    Notification(bottariId, bottariTitle, alarm),
                )
            }
}
