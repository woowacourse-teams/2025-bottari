package com.bottari.domain.usecase.bottari

import com.bottari.domain.model.bottari.personal.PersonalBottari
import com.bottari.domain.repository.AlarmRepository
import com.bottari.domain.repository.BottariItemRepository
import com.bottari.domain.repository.BottariRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FetchBottariDetailUseCase(
    private val bottariRepository: BottariRepository,
    private val bottariItemRepository: BottariItemRepository,
    private val alarmRepository: AlarmRepository,
) {
    operator fun invoke(id: Long): Flow<PersonalBottari> =
        combine(
            bottariRepository.fetchBottari(id),
            bottariItemRepository.fetchItems(id),
            alarmRepository.fetchAlarm(id),
        ) { bottari, items, alarm ->
            bottari.copy(
                alarm = alarm,
                items = items,
            )
        }
}
