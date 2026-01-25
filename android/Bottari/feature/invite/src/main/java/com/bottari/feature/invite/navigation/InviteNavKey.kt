package com.bottari.feature.invite.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class InviteNavKey(
    val inviteCode: String,
) : NavKey
