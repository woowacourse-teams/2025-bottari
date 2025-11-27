package com.bottari.core.local.entity.bottari

import androidx.room.Embedded
import androidx.room.Relation
import com.bottari.core.domain.model.bottari.personal.PersonalBottari

data class BottariWithAlarmAndItems(
    @Embedded
    val bottari: BottariEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "bottariId",
    )
    val items: List<ItemEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "bottariId",
    )
    val alarm: AlarmEntity?,
) {
    fun toDomain(): PersonalBottari =
        PersonalBottari(
            id = bottari.id,
            title = bottari.title,
            alarm = alarm?.toDomain(),
            items = items.map(ItemEntity::toDomain),
        )
}
