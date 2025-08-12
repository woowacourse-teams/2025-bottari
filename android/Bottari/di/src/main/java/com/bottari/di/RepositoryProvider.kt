package com.bottari.di

import com.bottari.data.repository.AlarmRepositoryImpl
import com.bottari.data.repository.AppConfigRepositoryImpl
import com.bottari.data.repository.BottariItemRepositoryImpl
import com.bottari.data.repository.BottariRepositoryImpl
import com.bottari.data.repository.BottariTemplateRepositoryImpl
import com.bottari.data.repository.MemberRepositoryImpl
import com.bottari.data.repository.ReportRepositoryImpl
import com.bottari.data.repository.TeamBottariRepositoryImpl
import com.bottari.domain.repository.AlarmRepository
import com.bottari.domain.repository.AppConfigRepository
import com.bottari.domain.repository.BottariItemRepository
import com.bottari.domain.repository.BottariRepository
import com.bottari.domain.repository.BottariTemplateRepository
import com.bottari.domain.repository.MemberRepository
import com.bottari.domain.repository.ReportRepository
import com.bottari.domain.repository.TeamBottariRepository

object RepositoryProvider {
    val memberRepository: MemberRepository by lazy {
        MemberRepositoryImpl(
            DataSourceProvider.memberRemoteDataSource,
            DataSourceProvider.memberIdentifierLocalDataSource,
        )
    }
    val bottariRepository: BottariRepository by lazy { BottariRepositoryImpl(DataSourceProvider.bottariRemoteDataSource) }
    val alarmRepository: AlarmRepository by lazy { AlarmRepositoryImpl(DataSourceProvider.alarmRemoteDataSource) }
    val bottariItemRepository: BottariItemRepository by lazy {
        BottariItemRepositoryImpl(
            DataSourceProvider.bottariItemRemoteDataSource,
        )
    }
    val bottariTemplateRepository: BottariTemplateRepository by lazy {
        BottariTemplateRepositoryImpl(
            DataSourceProvider.bottariTemplateRemoteSource,
        )
    }
    val appConfigRepository: AppConfigRepository by lazy {
        AppConfigRepositoryImpl(
            DataSourceProvider.appConfigDataSource,
        )
    }
    val reportRepository: ReportRepository by lazy {
        ReportRepositoryImpl(
            DataSourceProvider.reportRemoteDataSource,
        )
    }
    val teamBottariRepository: TeamBottariRepository by lazy {
        TeamBottariRepositoryImpl(
            DataSourceProvider.teamBottariRemoteDataSource,
        )
    }
}
