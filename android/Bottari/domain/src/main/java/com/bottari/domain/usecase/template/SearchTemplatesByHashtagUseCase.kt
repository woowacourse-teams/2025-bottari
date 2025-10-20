package com.bottari.domain.usecase.template

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.common.Pageable
import com.bottari.domain.repository.BottariTemplateRepository
import javax.inject.Inject

class SearchTemplatesByHashtagUseCase @Inject constructor(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(
        hashtagId: Long,
        pageable: Pageable<BottariTemplate>,
    ): Result<Pageable<BottariTemplate>> =
        bottariTemplateRepository.searchTemplatesByHashtag(
            hashtagId = hashtagId,
            pageable = pageable,
        )
}
