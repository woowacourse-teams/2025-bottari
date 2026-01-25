package com.bottari.bottari.di

import com.bottari.bottari.util.AlarmScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.bottari.common.util.AlarmScheduler as AlarmSchedulerContract

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmSchedulerModule {
    @Binds
    abstract fun bindAlarmScheduler(impl: AlarmScheduler): AlarmSchedulerContract
}
