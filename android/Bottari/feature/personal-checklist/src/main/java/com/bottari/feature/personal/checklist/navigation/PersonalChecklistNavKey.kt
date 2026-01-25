package com.bottari.feature.personal.checklist.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class PersonalChecklistNavKey(
    val bottariId: Long,
    val bottariTitle: String,
    val notificationFlag: Boolean = false,
) : NavKey
