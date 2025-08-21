package com.bottari.data.source.local

import com.bottari.data.local.MemberInfoDataStore
import com.google.android.gms.tasks.Tasks
import com.google.firebase.installations.FirebaseInstallations
import java.util.concurrent.TimeUnit

class MemberIdentifierLocalDataSourceImpl(
    private val memberInfoDataStore: MemberInfoDataStore,
) : MemberIdentifierLocalDataSource {
    private var cachedInstallationId: String? = null

    override fun getInstallationId(): Result<String> =
        runCatching {
            cachedInstallationId ?: initialize().also { cachedInstallationId = it }
        }

    override suspend fun saveMemberId(id: Long): Result<Unit> = runCatching { memberInfoDataStore.saveMemberId(id) }

    override suspend fun getMemberId(): Result<Long> =
        runCatching {
            requireNotNull(memberInfoDataStore.getMemberId()) { ERROR_MEMBER_ID_NULL }
        }

    private fun initialize(): String = Tasks.await(FirebaseInstallations.getInstance().id, 10, TimeUnit.SECONDS)

    companion object {
        private const val ERROR_MEMBER_ID_NULL = "[ERROR] 회원 ID가 null 입니다"
    }
}
