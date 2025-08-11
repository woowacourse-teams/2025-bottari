package com.bottari.data.source.local

interface MemberIdentifierLocalDataSource {
    fun getMemberIdentifier(): Result<String>
}
