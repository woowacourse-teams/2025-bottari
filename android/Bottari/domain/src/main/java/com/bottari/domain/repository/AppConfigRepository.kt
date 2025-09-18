package com.bottari.domain.repository

import com.bottari.domain.model.exception.BottariResult

interface AppConfigRepository {
    suspend fun savePermissionFlag(flag: Boolean): BottariResult<Unit>

    suspend fun getPermissionFlag(): BottariResult<Boolean>
}
