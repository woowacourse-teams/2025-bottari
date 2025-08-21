package com.bottari.di

import com.bottari.data.source.local.AppConfigDataSource
import com.bottari.data.source.local.AppConfigLocalDataSourceImpl
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.local.MemberIdentifierLocalDataSourceImpl
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
import com.bottari.data.source.remote.NotificationLocalDataSource
import com.bottari.data.source.remote.NotificationLocalDataSourceImpl
import com.bottari.data.source.remote.ReportRemoteDataSource
import com.bottari.data.source.remote.ReportRemoteDataSourceImpl
import com.bottari.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.data.source.remote.TeamBottariRemoteDataSourceImpl

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
    val alarmRemoteDataSource: AlarmRemoteDataSource by lazy {
        AlarmRemoteDataSourceImpl(
            NetworkProvider.alarmService,
        )
    }
    val bottariItemRemoteDataSource: BottariItemRemoteDataSource by lazy {
        BottariItemRemoteDataSourceImpl(
            NetworkProvider.bottariItemService,
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
    val notificationLocalDataSource: NotificationLocalDataSource by lazy {
        NotificationLocalDataSourceImpl(
            DatabaseProvider.notificationDatabase.notificationDao(),
        )
    }
    val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource by lazy {
        MemberIdentifierLocalDataSourceImpl()
    }
    val teamBottariRemoteDataSource: TeamBottariRemoteDataSource by lazy {
        TeamBottariRemoteDataSourceImpl(
            NetworkProvider.teamBottariService,
        )
    }
    val fcmRemoteDataSource: FcmRemoteDataSource by lazy {
        FcmRemoteDataSourceImpl(NetworkProvider.fcmService)
    }
    val eventRemoteDataSource: EventRemoteDataSource by lazy {
        EventRemoteDataSourceImpl(NetworkProvider.sseClient)
    }
}
