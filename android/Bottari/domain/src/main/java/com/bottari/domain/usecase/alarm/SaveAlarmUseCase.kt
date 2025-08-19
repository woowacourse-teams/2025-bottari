package com.bottari.domain.usecase.alarm

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.repository.AlarmRepository
import com.bottari.domain.repository.NotificationRepository

class SaveAlarmUseCase(
    private val alarmRepository: AlarmRepository,
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        bottariTitle: String,
        alarm: Alarm,
    ): Result<Unit> =
        alarmRepository
            .saveAlarm(alarm.id!!, alarm)
            .fold(
                onSuccess = {
                    notificationRepository.saveNotification(
                        Notification(
                            bottariId,
                            bottariTitle,
                            alarm,
                        ),
                    )
                    Result.success(Unit)
                },
                onFailure = { exception -> Result.failure(exception) },
            )
}
