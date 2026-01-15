package com.bottari.feature.team.edit.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class TeamEditNavKey(
    val bottariId: Long,
    val isNew: Boolean,
) : NavKey
