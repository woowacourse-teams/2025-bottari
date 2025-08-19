package com.bottari.domain.usecase.alarm

import com.bottari.domain.extension.flatMap
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.repository.AlarmRepository
import com.bottari.domain.repository.NotificationRepository
import java.lang.IllegalArgumentException

class SaveAlarmUseCase(
    private val alarmRepository: AlarmRepository,
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        bottariTitle: String,
        alarm: Alarm,
    ): Result<Unit> {
        val alarmId =
            alarm.id ?: return Result.failure(IllegalArgumentException(ERROR_REQUIRE_ALARM_ID))

        return alarmRepository
            .saveAlarm(alarmId, alarm)
            .flatMap {
                notificationRepository.saveNotification(
                    Notification(
                        bottariId,
                        bottariTitle,
                        alarm,
                    ),
                )
            }
    }

    companion object {
        private const val ERROR_REQUIRE_ALARM_ID = "[ERROR] 알람 ID가 존재하지 않습니다."
    }
}
