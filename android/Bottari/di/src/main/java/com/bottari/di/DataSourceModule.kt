package com.bottari.di

import com.bottari.data.local.AppConfigDataStore
import com.bottari.data.local.MemberInfoDataStore
import com.bottari.data.local.bottari.AlarmDao
import com.bottari.data.local.bottari.ItemDao
import com.bottari.data.network.SSEClient
import com.bottari.data.remote.RemoteConfig
import com.bottari.data.service.BottariTemplateService
import com.bottari.data.service.FcmService
import com.bottari.data.service.MemberService
import com.bottari.data.service.ReportService
import com.bottari.data.service.TeamBottariItemsService
import com.bottari.data.service.TeamBottariService
import com.bottari.data.service.TeamMemberService
import com.bottari.data.source.local.AppConfigDataSource
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.local.bottari.AlarmLocalDataSource
import com.bottari.data.source.local.bottari.ItemLocalDataSource
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.data.source.remote.EventRemoteDataSource
import com.bottari.data.source.remote.FcmRemoteDataSource
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.data.source.remote.RemoteConfigRemoteDataSource
import com.bottari.data.source.remote.ReportRemoteDataSource
import com.bottari.data.source.remote.TeamBottariItemsRemoteDataSource
import com.bottari.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.data.source.remote.TeamMemberRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindMemberRemoteDataSource(service: MemberService): MemberRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAlarmLocalDataSource(dao: AlarmDao): AlarmLocalDataSource

    @Binds
    @Singleton
    abstract fun bindItemLocalDataSource(dao: ItemDao): ItemLocalDataSource

    @Binds
    @Singleton
    abstract fun bindBottariTemplateRemoteDataSource(service: BottariTemplateService): BottariTemplateRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAppConfigDataSource(dataStore: AppConfigDataStore): AppConfigDataSource

    @Binds
    @Singleton
    abstract fun bindReportRemoteDataSource(service: ReportService): ReportRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMemberIdentifierLocalDataSource(dataStore: MemberInfoDataStore): MemberIdentifierLocalDataSource

    @Binds
    @Singleton
    abstract fun bindTeamBottariRemoteDataSource(service: TeamBottariService): TeamBottariRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTeamMemberRemoteDataSource(service: TeamMemberService): TeamMemberRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTeamBottariItemsRemoteDataSource(service: TeamBottariItemsService): TeamBottariItemsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindFcmRemoteDataSource(service: FcmService): FcmRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRemoteDataSource(config: RemoteConfig): RemoteConfigRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindEventRemoteDataSource(client: SSEClient): EventRemoteDataSource
}
