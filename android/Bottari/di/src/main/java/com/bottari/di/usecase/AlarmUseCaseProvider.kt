package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.alarm.SaveAlarmUseCase
import com.bottari.domain.usecase.alarm.UpdateAlarmActivateUseCase

object AlarmUseCaseProvider {
    val saveAlarmUseCase: SaveAlarmUseCase by lazy {
        SaveAlarmUseCase(
            RepositoryProvider.alarmRepository,
            RepositoryProvider.notificationRepository,
        )
    }
    val updateAlarmActivateUseCase: UpdateAlarmActivateUseCase by lazy {
        UpdateAlarmActivateUseCase(
            RepositoryProvider.alarmRepository,
            RepositoryProvider.notificationRepository,
        )
    }
}
