package com.bottari.domain.usecase.alarm

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.repository.AlarmRepository
import com.bottari.domain.repository.NotificationRepository
import java.lang.Exception

class ToggleAlarmStateUseCase(
    private val alarmRepository: AlarmRepository,
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        bottariTitle: String,
        alarm: Alarm,
        isActive: Boolean,
    ): Result<Unit> {
        val alarmId = alarm.id ?: return Result.failure(Exception(ERROR_REQUIRE_ALARM_ID))
        val toggleAlarmResult =
            if (isActive) {
                alarmRepository.activeAlarm(alarmId)
            } else {
                alarmRepository.inactiveAlarm(alarmId)
            }
        val notification = Notification(bottariId, bottariTitle, alarm.copy(isActive = isActive))
        return toggleAlarmResult.onSuccessSuspend {
            notificationRepository.saveNotification(
                notification,
            )
        }
    }

    private suspend fun <T> Result<T>.onSuccessSuspend(action: suspend (T) -> Result<Unit>): Result<Unit> =
        fold(
            onSuccess = { action(it) },
            onFailure = { exception -> Result.failure(exception) },
        )

    companion object {
        private const val ERROR_REQUIRE_ALARM_ID = "[ERROR] 알람 ID가 존재하지 않습니다."
    }
}
