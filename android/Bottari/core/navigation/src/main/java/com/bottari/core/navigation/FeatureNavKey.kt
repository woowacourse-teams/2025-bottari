package com.bottari.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface FeatureNavKey : NavKey {
    @Serializable
    data class InviteNavKey(
        val inviteCode: String,
    ) : FeatureNavKey

    @Serializable
    data class TemplateDetailNavKey(
        val templateId: Long,
        val isMyTemplate: Boolean,
        val isBookmark: Boolean,
    ) : FeatureNavKey

    @Serializable
    data object TemplateCreateNavKey : FeatureNavKey

    @Serializable
    data class PersonalChecklistNavKey(
        val bottariId: Long,
        val bottariTitle: String,
        val notificationFlag: Boolean = false,
    ) : FeatureNavKey

    @Serializable
    data class TeamChecklistNavKey(
        val bottariId: Long,
        val bottariTitle: String,
        val notificationFlag: Boolean = false,
    ) : FeatureNavKey

    @Serializable
    data class PersonalEditNavKey(
        val bottariId: Long,
        val isNew: Boolean,
    ) : FeatureNavKey

    @Serializable
    data class TeamEditNavKey(
        val bottariId: Long,
        val isNew: Boolean,
    ) : FeatureNavKey
}
