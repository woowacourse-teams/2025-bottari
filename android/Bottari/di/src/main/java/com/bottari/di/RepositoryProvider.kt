package com.bottari.di

import com.bottari.data.repository.BottariDetailRepositoryImpl
import com.bottari.data.repository.BottariRepositoryImpl
import com.bottari.data.repository.MemberRepositoryImpl

object RepositoryProvider {
    val memberRepository by lazy { MemberRepositoryImpl(DataSourceProvider.memberRemoteDataSource) }
    val bottariRepository by lazy { BottariRepositoryImpl(DataSourceProvider.bottariRemoteDataSource) }
    val bottariDetailRepository by lazy { BottariDetailRepositoryImpl(DataSourceProvider.bottariDetailRemoteDataSource) }
}
