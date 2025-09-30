package com.bottari.data.repository

import com.bottari.data.model.local.bottari.BottariEntity
import com.bottari.data.model.local.bottari.BottariWithAlarmAndItems
import com.bottari.data.source.local.bottari.BottariLocalDataSource
import com.bottari.domain.model.bottari.personal.PersonalBottari
import com.bottari.domain.repository.BottariRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BottariRepositoryImpl(
    private val bottariLocalDataSource: BottariLocalDataSource,
) : BottariRepository {
    override fun fetchBottaries(): Flow<List<PersonalBottari>> =
        bottariLocalDataSource
            .fetchBottaries()
            .map { bottaries -> bottaries.map(BottariWithAlarmAndItems::toDomain) }

    override fun findBottari(id: Long): Flow<PersonalBottari?> =
        bottariLocalDataSource
            .findBottari(id)
            .map { bottari -> bottari?.let(BottariWithAlarmAndItems::toDomain) }

    override suspend fun createBottari(title: String): Result<Long> =
        bottariLocalDataSource.createBottari(
            BottariEntity(title = title),
        )

    override suspend fun createBottariWithItems(
        title: String,
        itemNames: List<String>,
    ): Result<Long> =
        bottariLocalDataSource.createBottariWithItems(
            bottari = BottariEntity(title = title),
            itemNames = itemNames,
        )

    override suspend fun deleteBottari(id: Long): Result<Unit> = bottariLocalDataSource.deleteBottari(id)

    override suspend fun saveBottariTitle(
        id: Long,
        title: String,
    ): Result<Unit> =
        bottariLocalDataSource.updateBottariTitle(
            id,
            title,
        )
}
