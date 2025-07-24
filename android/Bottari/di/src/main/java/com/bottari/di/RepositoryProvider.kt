package com.bottari.di

import com.bottari.data.repository.AlarmRepositoryImpl
import com.bottari.data.repository.BottariItemRepositoryImpl
import com.bottari.data.repository.BottariRepositoryImpl
import com.bottari.data.repository.BottariTemplateRepositoryImpl
import com.bottari.data.repository.MemberRepositoryImpl

object RepositoryProvider {
    val memberRepository by lazy { MemberRepositoryImpl(DataSourceProvider.memberRemoteDataSource) }
    val bottariRepository by lazy { BottariRepositoryImpl(DataSourceProvider.bottariRemoteDataSource) }
    val bottariDetailRepository by lazy { BottariRepositoryImpl(DataSourceProvider.bottariDetailRemoteDataSource) }
    val alarmRepository by lazy { AlarmRepositoryImpl(DataSourceProvider.alarmRemoteDataSource) }
    val bottariItemRepository by lazy { BottariItemRepositoryImpl(DataSourceProvider.bottariItemRemoteDataSource) }
    val bottariTemplateRepository by lazy { BottariTemplateRepositoryImpl(DataSourceProvider.bottariTemplateRemoteSource) }
}
