package com.bottari.di

import com.bottari.data.source.local.AppConfigDataSource
import com.bottari.data.source.local.AppConfigLocalDataSourceImpl
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.local.MemberIdentifierLocalLocalDataSourceImpl
import com.bottari.data.source.remote.AlarmRemoteDataSource
import com.bottari.data.source.remote.AlarmRemoteDataSourceImpl
import com.bottari.data.source.remote.BottariItemRemoteDataSource
import com.bottari.data.source.remote.BottariItemRemoteDataSourceImpl
import com.bottari.data.source.remote.BottariRemoteDataSource
import com.bottari.data.source.remote.BottariRemoteDataSourceImpl
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.data.source.remote.BottariTemplateRemoteDataSourceImpl
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.data.source.remote.MemberRemoteDataSourceImpl
import com.bottari.data.source.remote.ReportRemoteDataSource
import com.bottari.data.source.remote.ReportRemoteDataSourceImpl

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
    val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource by lazy {
        MemberIdentifierLocalLocalDataSourceImpl()
    }
}
