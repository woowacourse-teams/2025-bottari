package com.bottari.data.source.local

interface UserInfoLocalDataSource {
    suspend fun saveUserId(userId: String): Result<Unit>

    suspend fun getUserId(): Result<String>
}
