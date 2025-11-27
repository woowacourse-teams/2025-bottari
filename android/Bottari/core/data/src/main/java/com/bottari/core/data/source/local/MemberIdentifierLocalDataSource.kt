package com.bottari.core.data.source.local

interface MemberIdentifierLocalDataSource {
    suspend fun saveMemberId(id: Long): Result<Unit>

    suspend fun getMemberId(): Result<Long>
}
