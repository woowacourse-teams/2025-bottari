package com.bottari.di

import android.content.Context
import com.bottari.data.local.bottari.AlarmDao
import com.bottari.data.local.bottari.BottariDatabase
import com.bottari.data.local.bottari.ItemDao
import com.bottari.data.local.tooltip.TooltipDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBottariDatabase(
        @ApplicationContext context: Context,
    ): BottariDatabase = BottariDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideAlarmDao(database: BottariDatabase): AlarmDao = database.alarmDao()

    @Provides
    @Singleton
    fun provideItemDao(database: BottariDatabase): ItemDao = database.itemDao()

    @Provides
    @Singleton
    fun provideTooltipDataStore(
        @ApplicationContext context: Context,
    ): TooltipDataStore = TooltipDataStore(context)
}
