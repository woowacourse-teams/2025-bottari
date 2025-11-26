package com.bottari.core.data.source.remote

import com.bottari.core.data.common.safeApiCall
import com.bottari.core.network.dto.hashtag.PopularHashtagResponse
import com.bottari.core.network.service.HashtagService
import javax.inject.Inject

class HashtagRemoteDataSourceImpl @Inject constructor(
    private val service: HashtagService,
) : HashtagRemoteDataSource {
    override suspend fun fetchPopularHashtags(): Result<List<PopularHashtagResponse>> = safeApiCall { service.fetchPopularHashtags() }
}
