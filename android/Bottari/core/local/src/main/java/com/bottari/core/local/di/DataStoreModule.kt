package com.bottari.core.local.di

import android.content.Context
import com.bottari.core.local.datastore.AppConfigDataStore
import com.bottari.core.local.datastore.MemberInfoDataStore
import com.bottari.core.local.datastore.TooltipDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideAppConfigDataStore(
        @ApplicationContext context: Context,
    ): AppConfigDataStore = AppConfigDataStore(context)

    @Provides
    @Singleton
    fun provideMemberInfoDataStore(
        @ApplicationContext context: Context,
    ): MemberInfoDataStore = MemberInfoDataStore(context)

    @Provides
    @Singleton
    fun provideTooltipDataStore(
        @ApplicationContext context: Context,
    ): TooltipDataStore = TooltipDataStore(context)
}
