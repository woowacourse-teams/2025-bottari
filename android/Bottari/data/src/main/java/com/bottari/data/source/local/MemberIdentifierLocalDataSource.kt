package com.bottari.data.source.local

interface MemberIdentifierLocalDataSource {
    suspend fun getMemberIdentifier(): Result<String>
}
