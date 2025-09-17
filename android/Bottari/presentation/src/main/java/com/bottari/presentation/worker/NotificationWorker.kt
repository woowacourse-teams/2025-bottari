package com.bottari.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.usecase.notification.GetNotificationsUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.model.alarm.NotificationUiModel
import com.bottari.presentation.util.AlarmScheduler.scheduleAlarm

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val getNotificationsUseCase: GetNotificationsUseCase,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result =
        getNotificationsUseCase()
            .mapCatching(::scheduleActiveAlarms)
            .fold(
                onSuccess = { Result.success() },
                onFailure = { exception ->
                    BottariLogger.error(exception.stackTraceToString(), exception)
                    Result.retry()
                },
            )

    private fun scheduleActiveAlarms(notifications: List<Notification>) =
        notifications.forEach { notification ->
            if (notification.alarm.isActive) {
                scheduleAlarm(notification = NotificationUiModel.fromDomain(notification))
            }
        }
}
