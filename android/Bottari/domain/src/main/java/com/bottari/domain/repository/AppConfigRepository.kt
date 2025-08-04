package com.bottari.domain.repository

interface AppConfigRepository {
    suspend fun savePermissionFlag(flag: Boolean): Result<Unit>

    suspend fun getPermissionFlag(): Result<Boolean>
}
