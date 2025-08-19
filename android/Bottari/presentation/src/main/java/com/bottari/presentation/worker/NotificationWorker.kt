package com.bottari.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.usecase.notification.GetNotificationsUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.mapper.NotificationMapper.toUiModel
import com.bottari.presentation.util.AlarmScheduler

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    private val getNotificationsUseCase: GetNotificationsUseCase by lazy { UseCaseProvider.getNotificationsUseCase }
    private val scheduler: AlarmScheduler by lazy { AlarmScheduler() }

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
                scheduler.scheduleAlarm(
                    notification.toUiModel(),
                )
            }
        }
}
