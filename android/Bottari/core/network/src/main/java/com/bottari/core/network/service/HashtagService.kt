package com.bottari.core.network.service

import com.bottari.core.network.dto.hashtag.PopularHashtagResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HashtagService {
    @GET("/hashtags/popular")
    suspend fun fetchPopularHashtags(
        @Query("limit") limit: Int = 10,
    ): Response<List<PopularHashtagResponse>>
}
