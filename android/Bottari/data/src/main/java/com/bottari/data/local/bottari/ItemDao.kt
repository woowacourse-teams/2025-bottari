package com.bottari.data.local.bottari

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.bottari.data.model.local.bottari.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM BottariItems WHERE bottariId = :bottariId")
    fun fetchItems(bottariId: Long): Flow<List<ItemEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun saveItem(item: ItemEntity)

    @Query("DELETE FROM BottariItems WHERE id = :id")
    suspend fun deleteItem(id: Long)

    @Query("UPDATE BottariItems SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateCheckState(
        id: Long,
        isChecked: Boolean,
    )

    @Query("UPDATE BottariItems SET isChecked = 0 WHERE bottariId = :bottariId")
    suspend fun resetCheckState(bottariId: Long)
}
