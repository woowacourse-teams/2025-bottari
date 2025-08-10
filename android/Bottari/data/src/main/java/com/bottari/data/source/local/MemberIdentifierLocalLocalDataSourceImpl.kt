package com.bottari.data.source.local

import com.google.android.gms.tasks.Tasks
import com.google.firebase.installations.FirebaseInstallations

class MemberIdentifierLocalLocalDataSourceImpl : MemberIdentifierLocalDataSource {
    override suspend fun getMemberIdentifier(): Result<String> =
        runCatching {
            Tasks.await(FirebaseInstallations.getInstance().id)
        }
}
