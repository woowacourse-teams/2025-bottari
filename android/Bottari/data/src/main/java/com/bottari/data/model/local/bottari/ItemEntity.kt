package com.bottari.data.model.local.bottari

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bottari.domain.model.bottari.item.ChecklistItem

@Entity(
    tableName = "BottariItems",
    foreignKeys = [
        ForeignKey(
            entity = BottariEntity::class,
            parentColumns = ["id"],
            childColumns = ["bottariId"],
            onDelete = CASCADE,
        ),
    ],
    indices = [Index("bottariId")],
)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "bottariId")
    val bottariId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "isChecked")
    val isChecked: Boolean,
) {
    fun toDomain(): ChecklistItem =
        ChecklistItem(
            id = id,
            name = name,
            isChecked = isChecked,
        )

    companion object {
        fun from(
            bottariId: Long,
            itemName: String,
        ): ItemEntity =
            ItemEntity(
                bottariId = bottariId,
                name = itemName,
                isChecked = false,
            )
    }
}
