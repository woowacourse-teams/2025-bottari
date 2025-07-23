package com.bottari.domain.repository

import com.bottari.domain.model.bottari.BottariItem

interface BottariItemRepository {
    suspend fun fetchChecklist(
        ssaid: String,
        bottariId: Long,
    ): Result<List<BottariItem>>

    suspend fun uncheckBottariItem(
        ssaid: String,
        bottariItemId: Long,
    ): Result<Unit>

    suspend fun checkBottariItem(
        ssaid: String,
        bottariItemId: Long,
    ): Result<Unit>
}
