package com.bottari.data.source.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.android.gms.tasks.Tasks
import com.google.firebase.installations.FirebaseInstallations
import java.util.concurrent.TimeUnit

class MemberIdentifierLocalDataSourceImpl(
    context: Context,
) : MemberIdentifierLocalDataSource {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(MEMBER_IDENTIFIER_PREFERENCES, Context.MODE_PRIVATE)

    private var cachedMemberIdentifier: String? = null

    override fun getMemberIdentifier(): Result<String> =
        runCatching {
            cachedMemberIdentifier ?: initialize().also { cachedMemberIdentifier = it }
        }

    override fun saveMemberId(id: Long) {
        sharedPreferences.edit { putLong(MEMBER_ID, id) }
    }

    override fun getMemberId(): Result<Long> {
        val id = sharedPreferences.getLong(MEMBER_ID, -1)
        return if (id == -1L) {
            Result.failure(IllegalStateException("No member id found"))
        } else {
            Result.success(id)
        }
    }

    private fun initialize(): String = Tasks.await(FirebaseInstallations.getInstance().id, 10, TimeUnit.SECONDS)

    companion object {
        private const val MEMBER_IDENTIFIER_PREFERENCES = "MEMBER_IDENTIFIER_PREFERENCES"
        private const val MEMBER_ID = "MEMBER_ID"
    }
}
