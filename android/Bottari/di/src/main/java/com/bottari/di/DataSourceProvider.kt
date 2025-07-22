package com.bottari.di

import com.bottari.data.source.remote.MemberRemoteDataSourceImpl

object DataSourceProvider {
    val memberRemoteDataSource by lazy { MemberRemoteDataSourceImpl(ServiceProvider.memberService) }
}
