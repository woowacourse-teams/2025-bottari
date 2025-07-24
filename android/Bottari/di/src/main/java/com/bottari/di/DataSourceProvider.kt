package com.bottari.di

import com.bottari.data.source.remote.AlarmRemoteDataSourceImpl
import com.bottari.data.source.remote.BottariDetailRemoteDataSourceImpl
import com.bottari.data.source.remote.BottariItemRemoteDataSource
import com.bottari.data.source.remote.BottariItemRemoteDataSourceImpl
import com.bottari.data.source.remote.BottariRemoteDataSource
import com.bottari.data.source.remote.BottariRemoteDataSourceImpl
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
    val bottariDetailRemoteDataSource by lazy { BottariDetailRemoteDataSourceImpl(ServiceProvider.bottariDetailService) }

    val alarmRemoteDataSource by lazy { AlarmRemoteDataSourceImpl(ServiceProvider.alarmService) }

    val bottariItemRemoteDataSource: BottariItemRemoteDataSource by lazy {
        BottariItemRemoteDataSourceImpl(
            ServiceProvider.bottariItemService,
        )
    }
}
