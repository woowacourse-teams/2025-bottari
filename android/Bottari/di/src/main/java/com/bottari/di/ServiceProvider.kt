package com.bottari.di

import com.bottari.data.network.RetrofitClient
import com.bottari.data.service.AlarmService
import com.bottari.data.service.BottariItemService
import com.bottari.data.service.BottariService
import com.bottari.data.service.BottariTemplateService
import com.bottari.data.service.MemberService
import com.bottari.data.service.ReportService

object ServiceProvider {
    val memberService: MemberService by lazy { RetrofitClient.create() }
    val bottariService: BottariService by lazy { RetrofitClient.create() }
    val alarmService: AlarmService by lazy { RetrofitClient.create() }
    val bottariItemService: BottariItemService by lazy { RetrofitClient.create() }
    val bottariTemplateService: BottariTemplateService by lazy { RetrofitClient.create() }
    val reportService: ReportService by lazy { RetrofitClient.create() }
}
