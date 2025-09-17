package com.bottari.data.service

import com.bottari.data.model.remote.bottari.BottariCreateRequest
import com.bottari.data.model.remote.bottari.BottariFetchResponse
import com.bottari.data.model.remote.bottari.BottariTitleUpdateRequest
import com.bottari.data.model.remote.bottari.BottariesFetchResponse
import com.bottari.domain.model.exception.BottariResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BottariService {
    @GET("/bottaries")
    suspend fun fetchBottaries(): BottariResult<List<BottariesFetchResponse>>

    @GET("/bottaries/{id}")
    suspend fun fetchBottari(
        @Path("id") id: Long,
    ): BottariResult<BottariFetchResponse>

    @POST("/bottaries")
    suspend fun createBottari(
        @Body request: BottariCreateRequest,
    ): BottariResult<Unit>

    @DELETE("/bottaries/{id}")
    suspend fun deleteBottari(
        @Path("id") id: Long,
    ): BottariResult<Unit>

    @PATCH("/bottaries/{id}")
    suspend fun saveBottariTitle(
        @Path("id") id: Long,
        @Body request: BottariTitleUpdateRequest,
    ): BottariResult<Unit>
}
