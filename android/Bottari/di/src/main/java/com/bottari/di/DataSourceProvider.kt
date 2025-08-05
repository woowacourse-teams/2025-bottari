package com.bottari.di

import com.bottari.data.source.local.AppConfigDataSource
import com.bottari.data.source.local.AppConfigLocalDataSourceImpl
import com.bottari.data.source.local.UserInfoLocalDataSource
import com.bottari.data.source.local.UserInfoLocalDataSourceImpl
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

object DataSourceProvider {
    val memberRemoteDataSource: MemberRemoteDataSource by lazy {
        MemberRemoteDataSourceImpl(
            ServiceProvider.memberService,
        )
    }
    val bottariRemoteDataSource: BottariRemoteDataSource by lazy {
        BottariRemoteDataSourceImpl(
            ServiceProvider.bottariService,
        )
    }
    val alarmRemoteDataSource: AlarmRemoteDataSource by lazy {
        AlarmRemoteDataSourceImpl(
            ServiceProvider.alarmService,
        )
    }
    val bottariItemRemoteDataSource: BottariItemRemoteDataSource by lazy {
        BottariItemRemoteDataSourceImpl(
            ServiceProvider.bottariItemService,
        )
    }
    val bottariTemplateRemoteSource: BottariTemplateRemoteDataSource by lazy {
        BottariTemplateRemoteDataSourceImpl(
            ServiceProvider.bottariTemplateService,
        )
    }
    val appConfigDataSource: AppConfigDataSource by lazy {
        AppConfigLocalDataSourceImpl(DataStoreProvider.provideAppConfigDataStore)
    }
    val userInfoLocalDataSource: UserInfoLocalDataSource by lazy {
        UserInfoLocalDataSourceImpl(DataStoreProvider.provideUserInfoDataStore)
    }
}
