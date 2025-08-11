package com.bottari.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bottari.domain.usecase.notification.GetNotificationsUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.mapper.NotificationMapper.toUiModel
import com.bottari.presentation.util.AlarmScheduler

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val getNotificationsUseCase: GetNotificationsUseCase,
) : CoroutineWorker(context, workerParams) {
    private val scheduler: AlarmScheduler by lazy { AlarmScheduler() }

    override suspend fun doWork(): Result =
        runCatching {
            getNotificationsUseCase()
                .onSuccess { notifications ->
                    notifications.forEach { notification -> scheduler.scheduleAlarm(notification.toUiModel()) }
                }.onFailure { error -> BottariLogger.error(error.message, error) }
        }.fold(
            onSuccess = { Result.success() },
            onFailure = { Result.failure() },
        )
}
