package com.bottari.di

import com.bottari.data.network.RetrofitClient
import com.bottari.data.service.MemberService

object ServiceProvider {
    val memberService: MemberService by lazy {
        RetrofitClient.create()
    }
}
