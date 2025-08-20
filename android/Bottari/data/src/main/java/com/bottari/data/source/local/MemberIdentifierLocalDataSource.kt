package com.bottari.data.source.local

interface MemberIdentifierLocalDataSource {
    fun getMemberIdentifier(): Result<String>

    fun saveMemberId(id: Long)

    fun getMemberId(): Result<Long>
}
