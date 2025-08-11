package com.bottari.data.local.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bottari.data.model.notification.NotificationEntity

@Dao
interface NotificationDao {
    @Query("SELECT * from notifications")
    suspend fun getNotifications(): List<NotificationEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveNotification(vararg notification: NotificationEntity)

    @Query("DELETE from notifications WHERE bottariId = :bottariId")
    suspend fun deleteNotification(bottariId: Long)
}
