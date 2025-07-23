package com.bottari.di

import com.bottari.data.network.RetrofitClient
import com.bottari.data.service.BottariItemService
import com.bottari.data.service.BottariService
import com.bottari.data.service.MemberService

object ServiceProvider {
    val memberService: MemberService by lazy { RetrofitClient.create() }
    val bottariService: BottariService by lazy { RetrofitClient.create() }
    val bottariItemService: BottariItemService by lazy { RetrofitClient.create() }
}
