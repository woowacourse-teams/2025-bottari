package com.bottari.core.data.repository

import com.bottari.core.data.source.local.BottariLocalDataSource
import com.bottari.core.domain.model.bottari.personal.PersonalBottari
import com.bottari.core.domain.model.notification.Notification
import com.bottari.core.domain.repository.BottariRepository
import com.bottari.core.local.entity.bottari.BottariEntity
import com.bottari.core.local.entity.bottari.BottariWithAlarm
import com.bottari.core.local.entity.bottari.BottariWithAlarmAndItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BottariRepositoryImpl @Inject constructor(
    private val bottariLocalDataSource: BottariLocalDataSource,
) : BottariRepository {
    override fun fetchBottaries(): Flow<List<PersonalBottari>> =
        bottariLocalDataSource
            .fetchBottaries()
            .map { bottaries -> bottaries.map(BottariWithAlarmAndItems::toDomain) }

    override suspend fun fetchNotifications(): Result<List<Notification>> =
        bottariLocalDataSource
            .fetchBottariesWithAlarm()
            .mapCatching(::toNotifications)

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

    private fun toNotifications(bottariesWithAlarm: List<BottariWithAlarm>): List<Notification> =
        bottariesWithAlarm.mapNotNull { bottariWithAlarm ->
            bottariWithAlarm.alarm?.let { alarm ->
                Notification(
                    bottariId = bottariWithAlarm.bottari.id,
                    bottariTitle = bottariWithAlarm.bottari.title,
                    alarm = alarm.toDomain(),
                )
            }
        }
}
