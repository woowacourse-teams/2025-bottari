package com.bottari.data.model.local.bottari

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bottari.domain.model.bottari.Bottari

@Entity("Bottaries")
data class BottariEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
) {
    companion object {
        fun fromDomain(bottari: Bottari): BottariEntity =
            BottariEntity(
                id = bottari.id,
                title = bottari.title,
            )
    }
}
