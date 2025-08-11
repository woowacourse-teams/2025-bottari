package com.bottari.data.source.local

import com.google.android.gms.tasks.Tasks
import com.google.firebase.installations.FirebaseInstallations
import java.util.concurrent.TimeUnit

class MemberIdentifierLocalDataSourceImpl : MemberIdentifierLocalDataSource {
    private var cachedMemberIdentifier: String? = null

    override fun getMemberIdentifier(): Result<String> =
        runCatching {
            cachedMemberIdentifier
                ?: initialize().also { cachedMemberIdentifier = it }
        }

    private fun initialize(): String = Tasks.await(FirebaseInstallations.getInstance().id, 10, TimeUnit.SECONDS)
}
