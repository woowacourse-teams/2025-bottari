package com.bottari.core.domain.usecase.bottari

import com.bottari.core.domain.model.bottari.personal.PersonalBottari
import com.bottari.core.domain.repository.BottariRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindBottariUseCase @Inject constructor(
    private val bottariRepository: BottariRepository,
) {
    operator fun invoke(id: Long): Flow<PersonalBottari?> = bottariRepository.findBottari(id)
}
