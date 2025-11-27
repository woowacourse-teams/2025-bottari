package com.bottari.core.local.database.bottari

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.bottari.core.local.entity.bottari.BottariEntity
import com.bottari.core.local.entity.bottari.BottariWithAlarm
import com.bottari.core.local.entity.bottari.BottariWithAlarmAndItems
import kotlinx.coroutines.flow.Flow

@Dao
interface BottariDao {
    @Transaction
    @Query("SELECT * FROM Bottaries ORDER BY createdAt DESC")
    fun fetchBottariesWithAlarmAndItems(): Flow<List<BottariWithAlarmAndItems>>

    @Transaction
    @Query("SELECT * FROM Bottaries")
    fun fetchBottariesWithAlarm(): List<BottariWithAlarm>

    @Transaction
    @Query("SELECT * FROM Bottaries WHERE id = :id")
    fun findBottariWithAlarmAndItems(id: Long): Flow<BottariWithAlarmAndItems?>

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
