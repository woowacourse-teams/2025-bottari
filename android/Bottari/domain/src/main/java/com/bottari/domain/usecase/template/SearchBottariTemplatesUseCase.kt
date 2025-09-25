package com.bottari.domain.usecase.template

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.common.Pageable
import com.bottari.domain.repository.BottariTemplateRepository

// TODO: 추후 제거 해야함
class SearchBottariTemplatesUseCase(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(
        query: String?,
        pageable: Pageable<BottariTemplate>,
    ): Result<Pageable<BottariTemplate>> =
        bottariTemplateRepository.fetchBottariTemplates(
            query = query,
            pageable = pageable,
        )
}
