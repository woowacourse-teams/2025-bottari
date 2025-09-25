package com.bottari.domain.usecase.bottari

import com.bottari.domain.model.bottari.personal.PersonalBottari
import com.bottari.domain.repository.BottariRepository
import kotlinx.coroutines.flow.Flow

class FetchBottariDetailsUseCase(
    private val bottariRepository: BottariRepository,
) {
    operator fun invoke(): Flow<List<PersonalBottari>> = bottariRepository.fetchBottaries()
}
