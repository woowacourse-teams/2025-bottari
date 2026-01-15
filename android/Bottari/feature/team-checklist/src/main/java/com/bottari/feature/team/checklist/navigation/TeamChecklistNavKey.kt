package com.bottari.feature.team.checklist.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class TeamChecklistNavKey(
    val bottariId: Long,
    val bottariTitle: String,
    val notificationFlag: Boolean = false,
) : NavKey
