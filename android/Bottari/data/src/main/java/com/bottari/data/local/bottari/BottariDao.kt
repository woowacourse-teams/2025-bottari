package com.bottari.data.local.bottari

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.bottari.data.model.local.bottari.BottariEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BottariDao {
    @Query("SELECT * FROM Bottaries ORDER BY createdAt DESC")
    fun fetchBottaries(): Flow<List<BottariEntity>>

    @Query("SELECT * FROM Bottaries WHERE id = :id")
    fun findBottari(id: Long): Flow<BottariEntity?>

    @Insert(onConflict = REPLACE)
    suspend fun createBottari(bottari: BottariEntity): Long

    @Query("DELETE FROM Bottaries WHERE id = :bottariId")
    suspend fun deleteBottari(bottariId: Long)

    @Query("UPDATE Bottaries SET title = :title WHERE id = :bottariId")
    suspend fun updateBottariTitle(
        bottariId: Long,
        title: String,
    )
}
