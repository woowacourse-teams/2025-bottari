package com.bottari.data.service

import com.bottari.data.model.remote.alarm.AlarmCreateRequest
import com.bottari.data.model.remote.alarm.AlarmSaveRequest
import com.bottari.domain.model.exception.BottariResult
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
    ): BottariResult<AlarmSaveRequest>

    @POST("/bottaries/{bottariId}/alarms")
    suspend fun createAlarm(
        @Path("bottariId") bottariId: Long,
        @Body alarmCreateRequest: AlarmCreateRequest,
    ): BottariResult<Unit>

    @PATCH("/alarms/{id}/active")
    suspend fun activeAlarm(
        @Path("id") id: Long,
    ): BottariResult<Unit>

    @PATCH("/alarms/{id}/inactive")
    suspend fun inactiveAlarm(
        @Path("id") id: Long,
    ): BottariResult<Unit>
}
