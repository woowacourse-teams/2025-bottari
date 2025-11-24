package com.bottari.core.local.database.bottari

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.bottari.core.local.entity.bottari.AlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM Alarms WHERE bottariId = :bottariId")
    fun findAlarm(bottariId: Long): Flow<AlarmEntity?>

    @Insert(onConflict = REPLACE)
    suspend fun saveAlarm(alarm: AlarmEntity)

    @Query("UPDATE Alarms SET isActive = :isActive WHERE id = :id")
    suspend fun updateAlarmActivate(
        id: Long,
        isActive: Boolean,
    )
}
