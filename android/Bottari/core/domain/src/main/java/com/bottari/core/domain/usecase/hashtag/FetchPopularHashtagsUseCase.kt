package com.bottari.core.domain.usecase.hashtag

import com.bottari.core.domain.model.bottari.template.PopularHashtag
import com.bottari.core.domain.repository.HashtagRepository
import javax.inject.Inject

class FetchPopularHashtagsUseCase @Inject constructor(
    private val repository: HashtagRepository,
) {
    suspend operator fun invoke(): Result<List<PopularHashtag>> = repository.fetchPopularHashtags()
}
