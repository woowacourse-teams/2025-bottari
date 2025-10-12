package com.bottari.presentation.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.bottari.domain.usecase.notification.FetchNotificationsUseCase

class NotificationWorkerFactory(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase,
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
                    fetchNotificationsUseCase = fetchNotificationsUseCase,
                )

            else -> null
        }
}
