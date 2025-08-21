package com.bottari.data.source.local

interface MemberIdentifierLocalDataSource {
    fun getInstallationId(): Result<String>

    suspend fun saveMemberId(id: Long): Result<Unit>

    suspend fun getMemberId(): Result<Long>
}
