package com.bottari.core.data.di

import com.bottari.core.data.source.local.AlarmLocalDataSource
import com.bottari.core.data.source.local.AlarmLocalDataSourceImpl
import com.bottari.core.data.source.local.AppConfigDataSource
import com.bottari.core.data.source.local.AppConfigLocalDataSourceImpl
import com.bottari.core.data.source.local.BookmarkLocalDataSource
import com.bottari.core.data.source.local.BookmarkLocalDataSourceImpl
import com.bottari.core.data.source.local.BottariLocalDataSource
import com.bottari.core.data.source.local.BottariLocalDataSourceImpl
import com.bottari.core.data.source.local.ItemLocalDataSource
import com.bottari.core.data.source.local.ItemLocalDataSourceImpl
import com.bottari.core.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.core.data.source.local.MemberIdentifierLocalDataSourceImpl
import com.bottari.core.data.source.local.TooltipLocalDataSource
import com.bottari.core.data.source.local.TooltipLocalDataSourceImpl
import com.bottari.core.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.core.data.source.remote.BottariTemplateRemoteDataSourceImpl
import com.bottari.core.data.source.remote.EventRemoteDataSource
import com.bottari.core.data.source.remote.EventRemoteDataSourceImpl
import com.bottari.core.data.source.remote.FcmRemoteDataSource
import com.bottari.core.data.source.remote.FcmRemoteDataSourceImpl
import com.bottari.core.data.source.remote.HashtagRemoteDataSource
import com.bottari.core.data.source.remote.HashtagRemoteDataSourceImpl
import com.bottari.core.data.source.remote.MemberRemoteDataSource
import com.bottari.core.data.source.remote.MemberRemoteDataSourceImpl
import com.bottari.core.data.source.remote.RemoteConfigRemoteDataSource
import com.bottari.core.data.source.remote.RemoteConfigRemoteDataSourceImpl
import com.bottari.core.data.source.remote.ReportRemoteDataSource
import com.bottari.core.data.source.remote.ReportRemoteDataSourceImpl
import com.bottari.core.data.source.remote.TeamBottariItemsRemoteDataSource
import com.bottari.core.data.source.remote.TeamBottariItemsRemoteDataSourceImpl
import com.bottari.core.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.core.data.source.remote.TeamBottariRemoteDataSourceImpl
import com.bottari.core.data.source.remote.TeamMemberRemoteDataSource
import com.bottari.core.data.source.remote.TeamMemberRemoteDataSourceImpl
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
