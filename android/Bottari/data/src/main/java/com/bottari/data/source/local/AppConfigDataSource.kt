package com.bottari.data.source.local

interface AppConfigDataSource {
    suspend fun savePermissionFlag(flag: Boolean): Result<Unit>

    suspend fun getPermissionFlag(): Result<Boolean>
}
