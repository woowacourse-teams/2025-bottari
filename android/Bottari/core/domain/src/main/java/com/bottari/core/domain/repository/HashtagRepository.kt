package com.bottari.core.domain.repository

import com.bottari.core.domain.model.bottari.template.PopularHashtag

interface HashtagRepository {
    suspend fun fetchPopularHashtags(): Result<List<PopularHashtag>>
}
