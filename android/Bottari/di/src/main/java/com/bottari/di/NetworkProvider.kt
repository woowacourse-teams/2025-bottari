package com.bottari.di

import com.bottari.data.common.util.PrettyJsonLogger
import com.bottari.data.network.RetrofitClient
import com.bottari.data.network.SSEClient
import com.bottari.data.network.SSEClientImpl
import com.bottari.data.network.interceptor.AuthInterceptor
import com.bottari.data.service.alarm.AlarmService
import com.bottari.data.service.bottari.BottariService
import com.bottari.data.service.bottari.item.BottariItemService
import com.bottari.data.service.bottari.template.BottariTemplateService
import com.bottari.data.service.fcm.FcmService
import com.bottari.data.service.member.MemberService
import com.bottari.data.service.report.ReportService
import com.bottari.data.service.team.bottari.TeamBottariService
import com.bottari.data.service.team.bottari.item.TeamBottariItemsService
import com.bottari.data.service.team.member.TeamMemberService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object NetworkProvider {
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor(PrettyJsonLogger()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    private val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(DataSourceProvider.memberIdentifierLocalDataSource)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    private val sseOkHttpClient: OkHttpClient by lazy {
        OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(0, TimeUnit.MILLISECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    private val retrofitClient: RetrofitClient by lazy { RetrofitClient(okHttpClient) }
    val sseClient: SSEClient by lazy { SSEClientImpl(sseOkHttpClient) }

    val memberService: MemberService by lazy { retrofitClient.create() }
    val bottariService: BottariService by lazy { retrofitClient.create() }
    val alarmService: AlarmService by lazy { retrofitClient.create() }
    val bottariItemService: BottariItemService by lazy { retrofitClient.create() }
    val bottariTemplateService: BottariTemplateService by lazy { retrofitClient.create() }
    val reportService: ReportService by lazy { retrofitClient.create() }
    val teamBottariService: TeamBottariService by lazy { retrofitClient.create() }

    val teamMemberService: TeamMemberService by lazy { retrofitClient.create() }
    val teamBottariItemsService: TeamBottariItemsService by lazy { retrofitClient.create() }
    val fcmService: FcmService by lazy { retrofitClient.create() }
}
