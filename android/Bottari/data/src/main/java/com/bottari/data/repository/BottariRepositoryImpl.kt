package com.bottari.data.repository

import com.bottari.data.model.local.bottari.BottariEntity
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
            .map { bottaries -> bottaries.map(BottariEntity::toDomain) }

    override fun findBottari(id: Long): Flow<PersonalBottari?> =
        bottariLocalDataSource
            .findBottari(id)
            .map { bottari -> bottari?.let(BottariEntity::toDomain) }

    override suspend fun saveBottari(title: String): Result<Long> =
        bottariLocalDataSource.createBottari(
            BottariEntity(title = title),
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
