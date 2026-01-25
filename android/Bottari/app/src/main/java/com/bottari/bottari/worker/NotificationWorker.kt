package com.bottari.bottari.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bottari.bottari.util.AlarmScheduler
import com.bottari.core.domain.model.notification.Notification
import com.bottari.core.domain.usecase.notification.FetchNotificationsUseCase
import com.bottari.logger.BottariLogger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val fetchNotificationsUseCase: FetchNotificationsUseCase,
    private val alarmScheduler: AlarmScheduler,
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

    private fun scheduleAlarms(notifications: List<Notification>) = notifications.forEach(alarmScheduler::scheduleAlarm)
}
