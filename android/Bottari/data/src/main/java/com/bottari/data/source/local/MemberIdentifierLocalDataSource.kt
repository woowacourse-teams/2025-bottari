package com.bottari.data.source.local

interface MemberIdentifierLocalDataSource {
    fun getInstallationId(): Result<String>

    fun saveMemberId(id: Long)

    fun getMemberId(): Result<Long>
}
