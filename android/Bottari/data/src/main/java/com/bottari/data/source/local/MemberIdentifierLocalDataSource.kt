package com.bottari.data.source.local

interface MemberIdentifierLocalDataSource {
    fun getInstallationId(): Result<String>

    fun saveMemberId(id: Long): Result<Unit>

    fun getMemberId(): Result<Long>
}
