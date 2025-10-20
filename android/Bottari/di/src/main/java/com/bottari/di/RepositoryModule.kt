package com.bottari.di

import com.bottari.data.repository.AlarmRepositoryImpl
import com.bottari.data.repository.AppConfigRepositoryImpl
import com.bottari.data.repository.BottariItemRepositoryImpl
import com.bottari.data.repository.BottariRepositoryImpl
import com.bottari.data.repository.BottariTemplateRepositoryImpl
import com.bottari.data.repository.EventRepositoryImpl
import com.bottari.data.repository.FcmRepositoryImpl
import com.bottari.data.repository.MemberRepositoryImpl
import com.bottari.data.repository.RemoteConfigRepositoryImpl
import com.bottari.data.repository.ReportRepositoryImpl
import com.bottari.data.repository.TeamBottariItemsRepositoryImpl
import com.bottari.data.repository.TeamBottariRepositoryImpl
import com.bottari.data.repository.TeamMemberRepositoryImpl
import com.bottari.data.repository.TooltipRepositoryImpl
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
import com.bottari.domain.repository.TooltipRepository
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
    abstract fun bindMemberRepository(impl: MemberRepositoryImpl): MemberRepository

    @Binds
    @Singleton
    abstract fun bindBottariRepository(impl: BottariRepositoryImpl): BottariRepository

    @Binds
    @Singleton
    abstract fun bindAlarmRepository(impl: AlarmRepositoryImpl): AlarmRepository

    @Binds
    @Singleton
    abstract fun bindBottariItemRepository(impl: BottariItemRepositoryImpl): BottariItemRepository

    @Binds
    @Singleton
    abstract fun bindBottariTemplateRepository(impl: BottariTemplateRepositoryImpl): BottariTemplateRepository

    @Binds
    @Singleton
    abstract fun bindAppConfigRepository(impl: AppConfigRepositoryImpl): AppConfigRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(impl: ReportRepositoryImpl): ReportRepository

    @Binds
    @Singleton
    abstract fun bindTeamBottariRepository(impl: TeamBottariRepositoryImpl): TeamBottariRepository

    @Binds
    @Singleton
    abstract fun bindTeamMemberRepository(impl: TeamMemberRepositoryImpl): TeamMemberRepository

    @Binds
    @Singleton
    abstract fun bindTeamBottariItemsRepository(impl: TeamBottariItemsRepositoryImpl): TeamBottariItemsRepository

    @Binds
    @Singleton
    abstract fun bindFcmRepository(impl: FcmRepositoryImpl): FcmRepository

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRepository(impl: RemoteConfigRepositoryImpl): RemoteConfigRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    @Binds
    @Singleton
    abstract fun bindTooltipRepository(impl: TooltipRepositoryImpl): TooltipRepository
}
