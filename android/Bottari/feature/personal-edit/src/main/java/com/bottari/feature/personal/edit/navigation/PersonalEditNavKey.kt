package com.bottari.feature.personal.edit.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class PersonalEditNavKey(
    val bottariId: Long,
    val isNew: Boolean,
) : NavKey
