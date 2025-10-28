package com.bottari.data.repository

import com.bottari.data.source.remote.HashtagRemoteDataSource
import com.bottari.domain.extension.mapCatching
import com.bottari.domain.model.bottari.template.PopularHashtag
import com.bottari.domain.repository.HashtagRepository
import javax.inject.Inject

class HashtagRepositoryImpl @Inject constructor(
    private val remoteDataSource: HashtagRemoteDataSource,
) : HashtagRepository {
    override suspend fun fetchPopularHashtags(): Result<List<PopularHashtag>> =
        remoteDataSource
            .fetchPopularHashtags()
            .mapCatching { response -> response.map { item -> item.toDomain() } }
}
