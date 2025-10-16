package com.bottari.di

import com.bottari.data.remote.FirebaseRemoteConfigImpl
import com.bottari.data.source.local.AppConfigDataSource
import com.bottari.data.source.local.AppConfigLocalDataSourceImpl
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
import com.bottari.data.source.remote.AlarmRemoteDataSource
import com.bottari.data.source.remote.AlarmRemoteDataSourceImpl
import com.bottari.data.source.remote.BottariItemRemoteDataSource
import com.bottari.data.source.remote.BottariItemRemoteDataSourceImpl
import com.bottari.data.source.remote.BottariRemoteDataSource
import com.bottari.data.source.remote.BottariRemoteDataSourceImpl
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.data.source.remote.BottariTemplateRemoteDataSourceImpl
import com.bottari.data.source.remote.EventRemoteDataSource
import com.bottari.data.source.remote.EventRemoteDataSourceImpl
import com.bottari.data.source.remote.FcmRemoteDataSource
import com.bottari.data.source.remote.FcmRemoteDataSourceImpl
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

object DataSourceProvider {
    val memberRemoteDataSource: MemberRemoteDataSource by lazy {
        MemberRemoteDataSourceImpl(
            NetworkProvider.memberService,
        )
    }
    val bottariRemoteDataSource: BottariRemoteDataSource by lazy {
        BottariRemoteDataSourceImpl(
            NetworkProvider.bottariService,
        )
    }

    val bottariLocalDataSource: BottariLocalDataSource by lazy {
        BottariLocalDataSourceImpl(
            DatabaseProvider.bottariDatabase,
        )
    }
    val alarmRemoteDataSource: AlarmRemoteDataSource by lazy {
        AlarmRemoteDataSourceImpl(
            NetworkProvider.alarmService,
        )
    }

    val alarmLocalDataSource: AlarmLocalDataSource by lazy {
        AlarmLocalDataSourceImpl(
            DatabaseProvider.bottariDatabase.alarmDao(),
        )
    }
    val bottariItemRemoteDataSource: BottariItemRemoteDataSource by lazy {
        BottariItemRemoteDataSourceImpl(
            NetworkProvider.bottariItemService,
        )
    }

    val bottariItemLocalDataSource: ItemLocalDataSource by lazy {
        ItemLocalDataSourceImpl(
            DatabaseProvider.bottariDatabase.itemDao(),
        )
    }
    val bottariTemplateRemoteSource: BottariTemplateRemoteDataSource by lazy {
        BottariTemplateRemoteDataSourceImpl(
            NetworkProvider.bottariTemplateService,
        )
    }
    val appConfigDataSource: AppConfigDataSource by lazy {
        AppConfigLocalDataSourceImpl(DataStoreProvider.provideAppConfigDataStore)
    }
    val reportRemoteDataSource: ReportRemoteDataSource by lazy {
        ReportRemoteDataSourceImpl(
            NetworkProvider.reportService,
        )
    }
    val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource by lazy {
        MemberIdentifierLocalDataSourceImpl(DataStoreProvider.provideMemberInfoDataStore)
    }
    val teamBottariRemoteDataSource: TeamBottariRemoteDataSource by lazy {
        TeamBottariRemoteDataSourceImpl(
            NetworkProvider.teamBottariService,
        )
    }
    val teamMemberRemoteDataSource: TeamMemberRemoteDataSource by lazy {
        TeamMemberRemoteDataSourceImpl(
            NetworkProvider.teamMemberService,
        )
    }
    val teamBottariItemRemoteDataSource: TeamBottariItemsRemoteDataSource by lazy {
        TeamBottariItemsRemoteDataSourceImpl(
            NetworkProvider.teamBottariItemsService,
        )
    }
    val fcmRemoteDataSource: FcmRemoteDataSource by lazy {
        FcmRemoteDataSourceImpl(NetworkProvider.fcmService)
    }
    val firebaseRemoteConfigDataSource: RemoteConfigRemoteDataSource by lazy {
        RemoteConfigRemoteDataSourceImpl(FirebaseRemoteConfigImpl())
    }
    val eventRemoteDataSource: EventRemoteDataSource by lazy {
        EventRemoteDataSourceImpl(NetworkProvider.sseClient)
    }

    val tooltipLocalDataSource: TooltipLocalDataSource by lazy {
        TooltipLocalDataSourceImpl(DatabaseProvider.tooltipDatabase)
    }
}
