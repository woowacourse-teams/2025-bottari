package com.bottari.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.usecase.notification.FetchNotificationsUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.util.AlarmScheduler.scheduleAlarm

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val fetchNotificationsUseCase: FetchNotificationsUseCase,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result =
        fetchNotificationsUseCase()
            .mapCatching(::scheduleAlarms)
            .fold(
                onSuccess = { Result.success() },
                onFailure = { exception ->
                    BottariLogger.error(exception.stackTraceToString(), exception)
                    Result.retry()
                },
            )

    private fun scheduleAlarms(notifications: List<Notification>) =
        notifications.forEach { notification -> scheduleAlarm(notification = notification) }
}
