package com.bottari.core.data.di

import com.bottari.core.data.repository.AlarmRepositoryImpl
import com.bottari.core.data.repository.AppConfigRepositoryImpl
import com.bottari.core.data.repository.BookmarkRepositoryImpl
import com.bottari.core.data.repository.BottariItemRepositoryImpl
import com.bottari.core.data.repository.BottariRepositoryImpl
import com.bottari.core.data.repository.BottariTemplateRepositoryImpl
import com.bottari.core.data.repository.EventRepositoryImpl
import com.bottari.core.data.repository.FcmRepositoryImpl
import com.bottari.core.data.repository.HashtagRepositoryImpl
import com.bottari.core.data.repository.MemberRepositoryImpl
import com.bottari.core.data.repository.RemoteConfigRepositoryImpl
import com.bottari.core.data.repository.ReportRepositoryImpl
import com.bottari.core.data.repository.TeamBottariItemsRepositoryImpl
import com.bottari.core.data.repository.TeamBottariRepositoryImpl
import com.bottari.core.data.repository.TeamMemberRepositoryImpl
import com.bottari.core.data.repository.TooltipRepositoryImpl
import com.bottari.core.domain.repository.AlarmRepository
import com.bottari.core.domain.repository.AppConfigRepository
import com.bottari.core.domain.repository.BookmarkRepository
import com.bottari.core.domain.repository.BottariItemRepository
import com.bottari.core.domain.repository.BottariRepository
import com.bottari.core.domain.repository.BottariTemplateRepository
import com.bottari.core.domain.repository.EventRepository
import com.bottari.core.domain.repository.FcmRepository
import com.bottari.core.domain.repository.HashtagRepository
import com.bottari.core.domain.repository.MemberRepository
import com.bottari.core.domain.repository.RemoteConfigRepository
import com.bottari.core.domain.repository.ReportRepository
import com.bottari.core.domain.repository.TeamBottariItemsRepository
import com.bottari.core.domain.repository.TeamBottariRepository
import com.bottari.core.domain.repository.TeamMemberRepository
import com.bottari.core.domain.repository.TooltipRepository
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

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository

    @Binds
    @Singleton
    abstract fun bindHashtagRepository(impl: HashtagRepositoryImpl): HashtagRepository
}
