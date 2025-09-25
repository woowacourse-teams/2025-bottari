package com.bottari.domain.usecase.bottari

import com.bottari.domain.model.bottari.personal.PersonalBottari
import com.bottari.domain.repository.AlarmRepository
import com.bottari.domain.repository.BottariItemRepository
import com.bottari.domain.repository.BottariRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class FetchBottariesUseCase(
    private val bottariRepository: BottariRepository,
    private val bottariItemRepository: BottariItemRepository,
    private val alarmRepository: AlarmRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<PersonalBottari>> =
        bottariRepository
            .fetchBottaries()
            .flatMapLatest { bottaries ->
                if (bottaries.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(bottaries.map(::combineBottari)) { it.toList() }
                }
            }

    private fun combineBottari(bottari: PersonalBottari): Flow<PersonalBottari> =
        combine(
            bottariItemRepository.fetchItems(bottari.id),
            alarmRepository.findAlarm(bottari.id),
        ) { items, alarm -> bottari.copy(alarm = alarm, items = items) }
}
