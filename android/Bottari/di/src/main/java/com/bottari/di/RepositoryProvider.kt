package com.bottari.di

import com.bottari.data.repository.MemberRepositoryImpl

object RepositoryProvider {
    val memberRepository by lazy { MemberRepositoryImpl(DataSourceProvider.memberRemoteDataSource) }
}
