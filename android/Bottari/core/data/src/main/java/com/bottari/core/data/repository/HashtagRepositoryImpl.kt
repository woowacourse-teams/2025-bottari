package com.bottari.core.data.repository

import com.bottari.core.data.source.remote.HashtagRemoteDataSource
import com.bottari.core.domain.model.bottari.template.PopularHashtag
import com.bottari.core.domain.repository.HashtagRepository
import com.bottari.core.network.dto.hashtag.PopularHashtagResponse
import javax.inject.Inject

class HashtagRepositoryImpl @Inject constructor(
    private val remoteDataSource: HashtagRemoteDataSource,
) : HashtagRepository {
    override suspend fun fetchPopularHashtags(): Result<List<PopularHashtag>> =
        remoteDataSource
            .fetchPopularHashtags()
            .mapCatching { response -> response.map(PopularHashtagResponse::toDomain) }
}
