package com.bottari.domain.usecase.hashtag

import com.bottari.domain.model.bottari.template.PopularHashtag
import com.bottari.domain.repository.HashtagRepository
import javax.inject.Inject

class FetchPopularHashtagsUseCase @Inject constructor(
    private val repository: HashtagRepository,
) {
    suspend operator fun invoke(): Result<List<PopularHashtag>> = repository.fetchPopularHashtags()
}
