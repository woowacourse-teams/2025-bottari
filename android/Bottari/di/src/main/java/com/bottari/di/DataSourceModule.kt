package com.bottari.di

import com.bottari.data.source.local.AppConfigDataSource
import com.bottari.data.source.local.AppConfigLocalDataSourceImpl
import com.bottari.data.source.local.BookmarkLocalDataSource
import com.bottari.data.source.local.BookmarkLocalDataSourceImpl
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.local.MemberIdentifierLocalDataSourceImpl
import com.bottari.data.source.local.bottari.AlarmLocalDataSource
import com.bottari.data.source.local.bottari.AlarmLocalDataSourceImpl
import com.bottari.data.source.local.bottari.BottariLocalDataSource
import com.bottari.data.source.local.bottari.BottariLocalDataSourceImpl
import com.bottari.data.source.local.bottari.ItemLocalDataSource
import com.bottari.data.source.local.bottari.ItemLocalDataSourceImpl
import com.bottari.data.source.local.tooltip.TooltipLocalDataSource
import com.bottari.data.source.local.tooltip.TooltipLocalDataSourceImpl
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.data.source.remote.BottariTemplateRemoteDataSourceImpl
import com.bottari.data.source.remote.EventRemoteDataSource
import com.bottari.data.source.remote.EventRemoteDataSourceImpl
import com.bottari.data.source.remote.FcmRemoteDataSource
import com.bottari.data.source.remote.FcmRemoteDataSourceImpl
import com.bottari.data.source.remote.HashtagRemoteDataSource
import com.bottari.data.source.remote.HashtagRemoteDataSourceImpl
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.data.source.remote.MemberRemoteDataSourceImpl
import com.bottari.data.source.remote.RemoteConfigRemoteDataSource
import com.bottari.data.source.remote.RemoteConfigRemoteDataSourceImpl
import com.bottari.data.source.remote.ReportRemoteDataSource
import com.bottari.data.source.remote.ReportRemoteDataSourceImpl
import com.bottari.data.source.remote.TeamBottariItemsRemoteDataSource
import com.bottari.data.source.remote.TeamBottariItemsRemoteDataSourceImpl
import com.bottari.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.data.source.remote.TeamBottariRemoteDataSourceImpl
import com.bottari.data.source.remote.TeamMemberRemoteDataSource
import com.bottari.data.source.remote.TeamMemberRemoteDataSourceImpl
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
    abstract fun bindMemberRemoteDataSource(impl: MemberRemoteDataSourceImpl): MemberRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBottariLocalDataSource(impl: BottariLocalDataSourceImpl): BottariLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAlarmLocalDataSource(impl: AlarmLocalDataSourceImpl): AlarmLocalDataSource

    @Binds
    @Singleton
    abstract fun bindItemLocalDataSource(impl: ItemLocalDataSourceImpl): ItemLocalDataSource

    @Binds
    @Singleton
    abstract fun bindBottariTemplateRemoteDataSource(impl: BottariTemplateRemoteDataSourceImpl): BottariTemplateRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAppConfigDataSource(impl: AppConfigLocalDataSourceImpl): AppConfigDataSource

    @Binds
    @Singleton
    abstract fun bindReportRemoteDataSource(impl: ReportRemoteDataSourceImpl): ReportRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMemberIdentifierLocalDataSource(impl: MemberIdentifierLocalDataSourceImpl): MemberIdentifierLocalDataSource

    @Binds
    @Singleton
    abstract fun bindTeamBottariRemoteDataSource(impl: TeamBottariRemoteDataSourceImpl): TeamBottariRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTeamMemberRemoteDataSource(impl: TeamMemberRemoteDataSourceImpl): TeamMemberRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTeamBottariItemsRemoteDataSource(impl: TeamBottariItemsRemoteDataSourceImpl): TeamBottariItemsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindFcmRemoteDataSource(impl: FcmRemoteDataSourceImpl): FcmRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRemoteDataSource(impl: RemoteConfigRemoteDataSourceImpl): RemoteConfigRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindEventRemoteDataSource(impl: EventRemoteDataSourceImpl): EventRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTooltipLocalDataSource(impl: TooltipLocalDataSourceImpl): TooltipLocalDataSource

    @Binds
    @Singleton
    abstract fun bindBookmarkLocalDataSource(impl: BookmarkLocalDataSourceImpl): BookmarkLocalDataSource

    @Binds
    @Singleton
    abstract fun bindHashtagRemoteDataSource(impl: HashtagRemoteDataSourceImpl): HashtagRemoteDataSource
}
