package com.bottari.domain.usecase.template

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.common.Pageable
import com.bottari.domain.repository.BottariTemplateRepository
import javax.inject.Inject

class SearchTemplatesByTitleUseCase @Inject constructor(
    private val bottariTemplateRepository: BottariTemplateRepository,
) {
    suspend operator fun invoke(
        query: String,
        pageable: Pageable<BottariTemplate>,
    ): Result<Pageable<BottariTemplate>> {
        println(
            """
            ---
            SearchTemplatesByTitleUseCase
            query: $query
            pageable: $pageable
                        ---
            """.trimIndent(),
        )
        return bottariTemplateRepository.searchTemplatesByTitle(
            title = query,
            pageable = pageable,
        )
    }
}
