package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.alarm.FindAlarmUseCase
import com.bottari.domain.usecase.alarm.SaveAlarmUseCase
import com.bottari.domain.usecase.alarm.UpdateAlarmActivateUseCase

object AlarmUseCaseProvider {
    val findAlarmUseCase: FindAlarmUseCase by lazy {
        FindAlarmUseCase(
            RepositoryProvider.alarmRepository,
        )
    }
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
