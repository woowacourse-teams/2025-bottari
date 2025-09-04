package com.bottari.presentation.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.bottari.domain.usecase.notification.GetNotificationsUseCase

class NotificationWorkerFactory(
    private val getNotificationsUseCase: GetNotificationsUseCase,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? =
        when (workerClassName) {
            NotificationWorker::class.qualifiedName ->
                NotificationWorker(
                    context = appContext,
                    workerParams = workerParameters,
                    getNotificationsUseCase = getNotificationsUseCase,
                )

            else -> null
        }
}
