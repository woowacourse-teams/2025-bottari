package com.bottari.domain.usecase.alarm

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.exception.mapCatching
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.repository.AlarmRepository
import com.bottari.domain.repository.NotificationRepository

class ToggleAlarmStateUseCase(
    private val alarmRepository: AlarmRepository,
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        bottariTitle: String,
        alarm: Alarm,
        isActive: Boolean,
    ): BottariResult<Unit> {
        val alarmId =
            alarm.id
                ?: return BottariResult.ApiError(BottariException.AlarmException.NotFoundException)
        val toggleAlarmResult = toggleAlarmState(isActive, alarmId)
        return toggleAlarmResult.mapCatching {
            notificationRepository.saveNotification(
                Notification(
                    bottariId,
                    bottariTitle,
                    alarm.copy(isActive = isActive),
                ),
            )
        }
    }

    private suspend fun toggleAlarmState(
        isActive: Boolean,
        alarmId: Long,
    ): BottariResult<Unit> =
        if (isActive) {
            alarmRepository.activeAlarm(alarmId)
        } else {
            alarmRepository.inactiveAlarm(alarmId)
        }
}
