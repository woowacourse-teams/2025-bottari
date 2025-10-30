package com.bottari.domain.repository

import com.bottari.domain.model.bottari.template.PopularHashtag

interface HashtagRepository {
    suspend fun fetchPopularHashtags(): Result<List<PopularHashtag>>
}
