package com.bottari.di.usecase

import com.bottari.di.RepositoryProvider
import com.bottari.domain.usecase.alarm.SaveAlarmUseCase
import com.bottari.domain.usecase.alarm.ToggleAlarmStateUseCase

object AlarmUseCaseProvider {
    val saveAlarmUseCase: SaveAlarmUseCase by lazy {
        SaveAlarmUseCase(
            RepositoryProvider.alarmRepository,
            RepositoryProvider.notificationRepository,
        )
    }
    val toggleAlarmStateUseCase: ToggleAlarmStateUseCase by lazy {
        ToggleAlarmStateUseCase(
            RepositoryProvider.alarmRepository,
            RepositoryProvider.notificationRepository,
        )
    }
}
