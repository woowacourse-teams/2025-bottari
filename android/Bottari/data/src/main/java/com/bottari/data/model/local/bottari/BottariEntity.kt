package com.bottari.data.model.local.bottari

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bottari.domain.model.bottari.personal.PersonalBottari

@Entity("Bottaries")
data class BottariEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): PersonalBottari =
        PersonalBottari(
            id = id,
            title = title,
            alarm = null,
            items = emptyList(),
        )

    companion object {
        fun fromDomain(bottari: PersonalBottari): BottariEntity =
            BottariEntity(
                id = bottari.id,
                title = bottari.title,
            )
    }
}
