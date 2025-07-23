package com.bottari.data.service

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AlarmService {
    @PATCH("/alarms/{id}/{state}")
    suspend fun toggleAlarmState(
        @Path("id") id: Long,
        @Path("state") state: String,
        @Header("ssaid") ssaid: String,
    ): Response<Boolean>
}
