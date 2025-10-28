package com.bottari.data.source.remote

import com.bottari.data.model.remote.hashtag.PopularHashtagResponse

interface HashtagRemoteDataSource {
    suspend fun fetchPopularHashtags(): Result<List<PopularHashtagResponse>>
}
