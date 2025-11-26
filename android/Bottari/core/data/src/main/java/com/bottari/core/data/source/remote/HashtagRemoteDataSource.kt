package com.bottari.core.data.source.remote

import com.bottari.core.network.dto.hashtag.PopularHashtagResponse

interface HashtagRemoteDataSource {
    suspend fun fetchPopularHashtags(): Result<List<PopularHashtagResponse>>
}
