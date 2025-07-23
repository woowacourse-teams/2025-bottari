package com.bottari.di

import com.bottari.data.network.RetrofitClient
import com.bottari.data.service.AlarmService
import com.bottari.data.service.BottariDetailService
import com.bottari.data.service.BottariService
import com.bottari.data.service.MemberService

object ServiceProvider {
    val memberService: MemberService by lazy { RetrofitClient.create() }
    val bottariService: BottariService by lazy { RetrofitClient.create() }
    val bottariDetailService: BottariDetailService by lazy { RetrofitClient.create() }
    val alarmService: AlarmService by lazy { RetrofitClient.create() }
}
