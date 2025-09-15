package com.bottari.data.service

import com.bottari.data.model.alarm.request.AlarmCreateRequest
import com.bottari.data.model.alarm.request.AlarmSaveRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AlarmService {
    @PUT("/alarms/{id}")
    suspend fun saveAlarm(
        @Path("id") id: Long,
        @Body alarmSaveRequest: AlarmSaveRequest,
    ): Response<Unit>

    @POST("/bottaries/{bottariId}/alarms")
    suspend fun createAlarm(
        @Path("bottariId") bottariId: Long,
        @Body alarmCreateRequest: AlarmCreateRequest,
    ): Response<Unit>

    @PATCH("/alarms/{id}/active")
    suspend fun activeAlarm(
        @Path("id") id: Long,
    ): Response<Unit>

    @PATCH("/alarms/{id}/inactive")
    suspend fun inactiveAlarm(
        @Path("id") id: Long,
    ): Response<Unit>
}
