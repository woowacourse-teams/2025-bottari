package com.bottari.di

import com.bottari.data.repository.BottariItemRepositoryImpl
import com.bottari.data.repository.BottariRepositoryImpl
import com.bottari.data.repository.MemberRepositoryImpl

object RepositoryProvider {
    val memberRepository by lazy { MemberRepositoryImpl(DataSourceProvider.memberRemoteDataSource) }
    val bottariRepository by lazy { BottariRepositoryImpl(DataSourceProvider.bottariRemoteDataSource) }
    val bottariItemRepository by lazy { BottariItemRepositoryImpl(DataSourceProvider.bottariItemRemoteDataSource) }
}
