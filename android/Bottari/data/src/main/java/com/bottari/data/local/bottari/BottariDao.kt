package com.bottari.data.local.bottari

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.bottari.data.model.local.bottari.BottariEntity

@Dao
interface BottariDao {
    @Insert(onConflict = REPLACE)
    suspend fun createBottari(title: String)

    @Insert(onConflict = REPLACE)
    suspend fun saveBottari(bottari: BottariEntity)

    @Query("DELETE FROM Bottaries WHERE id = :bottariId")
    suspend fun deleteBottari(bottariId: Long)

    @Query("UPDATE Bottaries SET title = :title WHERE id = :bottariId")
    suspend fun updateBottariTitle(
        bottariId: Long,
        title: String,
    )
}
