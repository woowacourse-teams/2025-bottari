package com.bottari.di

import com.bottari.data.source.local.AppConfigDataSource
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.local.bottari.AlarmLocalDataSource
import com.bottari.data.source.local.bottari.BottariLocalDataSource
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
import com.bottari.domain.repository.AlarmRepository
import com.bottari.domain.repository.AppConfigRepository
import com.bottari.domain.repository.BottariItemRepository
import com.bottari.domain.repository.BottariRepository
import com.bottari.domain.repository.BottariTemplateRepository
import com.bottari.domain.repository.EventRepository
import com.bottari.domain.repository.FcmRepository
import com.bottari.domain.repository.MemberRepository
import com.bottari.domain.repository.RemoteConfigRepository
import com.bottari.domain.repository.ReportRepository
import com.bottari.domain.repository.TeamBottariItemsRepository
import com.bottari.domain.repository.TeamBottariRepository
import com.bottari.domain.repository.TeamMemberRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMemberRepository(
        memberDataSource: MemberRemoteDataSource,
        memberIdentifierDataSource: MemberIdentifierLocalDataSource,
    ): MemberRepository

    @Binds
    @Singleton
    abstract fun bindBottariRepository(dataSource: BottariLocalDataSource): BottariRepository

    @Binds
    @Singleton
    abstract fun bindAlarmRepository(dataSource: AlarmLocalDataSource): AlarmRepository

    @Binds
    @Singleton
    abstract fun bindBottariItemRepository(dataSource: ItemLocalDataSource): BottariItemRepository

    @Binds
    @Singleton
    abstract fun bindBottariTemplateRepository(dataSource: BottariTemplateRemoteDataSource): BottariTemplateRepository

    @Binds
    @Singleton
    abstract fun bindAppConfigRepository(dataSource: AppConfigDataSource): AppConfigRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(dataSource: ReportRemoteDataSource): ReportRepository

    @Binds
    @Singleton
    abstract fun bindTeamBottariRepository(dataSource: TeamBottariRemoteDataSource): TeamBottariRepository

    @Binds
    @Singleton
    abstract fun bindTeamMemberRepository(dataSource: TeamMemberRemoteDataSource): TeamMemberRepository

    @Binds
    @Singleton
    abstract fun bindTeamBottariItemsRepository(dataSource: TeamBottariItemsRemoteDataSource): TeamBottariItemsRepository

    @Binds
    @Singleton
    abstract fun bindFcmRepository(
        fcmDataSource: FcmRemoteDataSource,
        memberIdentifierDataSource: MemberIdentifierLocalDataSource,
    ): FcmRepository

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRepository(dataSource: RemoteConfigRemoteDataSource): RemoteConfigRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(dataSource: EventRemoteDataSource): EventRepository
}
