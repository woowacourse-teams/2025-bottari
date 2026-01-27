package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.remote.hashtag.PopularHashtagResponse
import com.bottari.data.service.HashtagService
import javax.inject.Inject

class HashtagRemoteDataSourceImpl @Inject constructor(
    private val service: HashtagService,
) : HashtagRemoteDataSource {
    override suspend fun fetchPopularHashtags(): Result<List<PopularHashtagResponse>> = safeApiCall { service.fetchPopularHashtags() }
}
