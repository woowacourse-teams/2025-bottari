package com.bottari.di

import com.bottari.data.common.util.PrettyJsonLogger
import com.bottari.data.network.RetrofitClient
import com.bottari.data.network.interceptor.AuthInterceptor
import com.bottari.data.service.AlarmService
import com.bottari.data.service.BottariItemService
import com.bottari.data.service.BottariService
import com.bottari.data.service.BottariTemplateService
import com.bottari.data.service.MemberService
import com.bottari.data.service.ReportService
import okhttp3.logging.HttpLoggingInterceptor

object NetworkProvider {
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor(PrettyJsonLogger()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    private val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(DataSourceProvider.memberIdentifierLocalDataSource)
    }

    private val okHttpClient: okhttp3.OkHttpClient by lazy {
        okhttp3.OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    private val retrofitClient: RetrofitClient by lazy { RetrofitClient(okHttpClient) }

    val memberService: MemberService by lazy { retrofitClient.create() }
    val bottariService: BottariService by lazy { retrofitClient.create() }
    val alarmService: AlarmService by lazy { retrofitClient.create() }
    val bottariItemService: BottariItemService by lazy { retrofitClient.create() }
    val bottariTemplateService: BottariTemplateService by lazy { retrofitClient.create() }
    val reportService: ReportService by lazy { retrofitClient.create() }
}
