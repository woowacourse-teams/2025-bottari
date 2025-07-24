package com.bottari.data.service

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AlarmService {
    @PATCH("/alarms/{id}/active")
    suspend fun activeAlarm(
        @Header("ssaid") ssaid: String,
        @Path("id") id: Long,
    ): Response<Unit>

    @PATCH("/alarms/{id}/inactive")
    suspend fun inactiveAlarm(
        @Header("ssaid") ssaid: String,
        @Path("id") id: Long,
    ): Response<Unit>
}
