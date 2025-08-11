package com.bottari.data.source.local

import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MemberIdentifierLocalDataSourceImpl(
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : MemberIdentifierLocalDataSource {
    private var cachedMemberIdentifier: String? = null

    init {
        coroutineScope.launch { initialize() }
    }

    override fun getMemberIdentifier(): Result<String> =
        runCatching {
            cachedMemberIdentifier
                ?: error(ERROR_FETCH_MEMBER_IDENTIFIER)
        }

    private suspend fun initialize() {
        cachedMemberIdentifier = fetchMemberIdentifier()
    }

    private suspend fun fetchMemberIdentifier(): String = FirebaseInstallations.getInstance().id.await()

    companion object {
        private const val ERROR_FETCH_MEMBER_IDENTIFIER = "[ERROR] 유저 식별자가 초기화되지 않았습니다."
    }
}
