package com.bottari.core.data.source.local

import com.bottari.core.local.datastore.MemberInfoDataStore
import javax.inject.Inject

class MemberIdentifierLocalDataSourceImpl @Inject constructor(
    private val memberInfoDataStore: MemberInfoDataStore,
) : MemberIdentifierLocalDataSource {
    override suspend fun saveMemberId(id: Long): Result<Unit> = memberInfoDataStore.saveMemberId(id)

    override suspend fun getMemberId(): Result<Long> = memberInfoDataStore.getMemberId()
}
