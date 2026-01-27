package com.bottari.data.service

import com.bottari.data.model.remote.hashtag.PopularHashtagResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HashtagService {
    @GET("/hashtags/popular")
    suspend fun fetchPopularHashtags(
        @Query("limit") limit: Int = 10,
    ): Response<List<PopularHashtagResponse>>
}
