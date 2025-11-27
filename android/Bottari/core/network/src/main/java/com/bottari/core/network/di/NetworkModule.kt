package com.bottari.core.network.di

import com.bottari.core.network.client.RetrofitClient
import com.bottari.core.network.client.SSEClient
import com.bottari.core.network.client.SSEClientImpl
import com.bottari.core.network.client.interceptor.AuthInterceptor
import com.bottari.core.network.client.interceptor.FirebaseInstallationIdProvider
import com.bottari.core.network.remote.FirebaseRemoteConfigImpl
import com.bottari.core.network.remote.RemoteConfig
import com.bottari.core.network.service.BottariTemplateService
import com.bottari.core.network.service.FcmService
import com.bottari.core.network.service.HashtagService
import com.bottari.core.network.service.MemberService
import com.bottari.core.network.service.ReportService
import com.bottari.core.network.service.TeamBottariItemsService
import com.bottari.core.network.service.TeamBottariService
import com.bottari.core.network.service.TeamMemberService
import com.bottari.data.common.util.PrettyJsonLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class SSEClientType

@Qualifier
annotation class MainClientType

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(PrettyJsonLogger()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideInstallationIdProvider(): FirebaseInstallationIdProvider = FirebaseInstallationIdProvider()

    @Provides
    @Singleton
    fun provideAuthInterceptor(installationIdProvider: FirebaseInstallationIdProvider): AuthInterceptor =
        AuthInterceptor(installationIdProvider)

    @Provides
    @Singleton
    @MainClientType
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    @SSEClientType
    fun provideOkHttpSSEClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(0, TimeUnit.MILLISECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()

    @Provides
    @Singleton
    fun provideRetrofitClient(
        @MainClientType okHttpClient: OkHttpClient,
    ): RetrofitClient = RetrofitClient(okHttpClient)

    @Provides
    @Singleton
    fun provideSSEClient(
        @SSEClientType okHttpClient: OkHttpClient,
    ): SSEClient = SSEClientImpl(okHttpClient)

    @Provides
    @Singleton
    fun provideRetrofit(
        @MainClientType okHttpClient: OkHttpClient,
    ): Retrofit = RetrofitClient(okHttpClient).retrofit

    @Provides
    @Singleton
    fun provideMemberService(retrofit: Retrofit): MemberService = retrofit.create(MemberService::class.java)

    @Provides
    @Singleton
    fun provideBottariTemplateService(retrofit: Retrofit): BottariTemplateService = retrofit.create(BottariTemplateService::class.java)

    @Provides
    @Singleton
    fun provideReportService(retrofit: Retrofit): ReportService = retrofit.create(ReportService::class.java)

    @Provides
    @Singleton
    fun provideTeamBottariService(retrofit: Retrofit): TeamBottariService = retrofit.create(TeamBottariService::class.java)

    @Provides
    @Singleton
    fun provideTeamMemberService(retrofit: Retrofit): TeamMemberService = retrofit.create(TeamMemberService::class.java)

    @Provides
    @Singleton
    fun provideTeamBottariItemsService(retrofit: Retrofit): TeamBottariItemsService = retrofit.create(TeamBottariItemsService::class.java)

    @Provides
    @Singleton
    fun provideFcmService(retrofit: Retrofit): FcmService = retrofit.create(FcmService::class.java)

    @Provides
    @Singleton
    fun provideHashtagService(retrofit: Retrofit): HashtagService = retrofit.create(HashtagService::class.java)

    @Provides
    @Singleton
    fun provideRemoteConfig(): RemoteConfig = FirebaseRemoteConfigImpl()
}
