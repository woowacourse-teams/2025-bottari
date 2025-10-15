package com.bottari.presentation.service.fcm

import com.bottari.logger.BottariLogger
import com.bottari.presentation.R
import com.bottari.presentation.service.fcm.FcmMessage.Companion.KEY_EVENT
import com.bottari.presentation.service.fcm.FcmMessage.Companion.KEY_RESOURCE
import com.bottari.presentation.util.NotificationHelper
import org.json.JSONObject

sealed class FcmMessage {
    abstract fun sendNotification(notificationHelper: NotificationHelper)

    data class TeamMemberDelete(
        val bottariId: Long,
        val bottariName: String,
        val exitMemberId: Long,
        val exitMemberName: String,
        val publishedAt: String,
    ) : FcmMessage() {
        override fun sendNotification(notificationHelper: NotificationHelper) {
            notificationHelper.sendTeamMessage(
                bottariId,
                bottariName,
                R.string.notification_team_bottari_notification_exit_message,
                exitMemberName,
                bottariName,
            )
        }

        companion object {
            const val TYPE = "TEAM_MEMBER_DELETE"
            private const val KEY_BOTTARI_ID = "bottariId"
            private const val KEY_BOTTARI_NAME = "bottariName"
            private const val KEY_EXIT_MEMBER_ID = "exitMemberId"
            private const val KEY_EXIT_MEMBER_NAME = "exitMemberName"
            private const val KEY_PUBLISHED_AT = "publishedAt"

            fun fromData(data: JSONObject): TeamMemberDelete =
                TeamMemberDelete(
                    data.getLong(KEY_BOTTARI_ID),
                    data.getString(KEY_BOTTARI_NAME),
                    data.getLong(KEY_EXIT_MEMBER_ID),
                    data.getString(KEY_EXIT_MEMBER_NAME),
                    data.getString(KEY_PUBLISHED_AT),
                )
        }
    }

    data class SharedItemInfoRemind(
        val teamBottariId: Long,
        val teamBottariTitle: String,
        val teamItemName: String,
    ) : FcmMessage() {
        override fun sendNotification(notificationHelper: NotificationHelper) {
            notificationHelper.sendTeamMessage(
                teamBottariId,
                teamBottariTitle,
                R.string.notification_team_bottari_remind_by_item_message,
                teamItemName,
            )
        }

        companion object {
            const val TYPE = "SHARED_ITEM_INFO_REMIND"
            private const val KEY_TEAM_ID = "teamBottariId"
            private const val KEY_TEAM_TITLE = "teamBottariTitle"
            private const val KEY_ITEM_NAME = "teamItemName"

            fun fromData(data: JSONObject): SharedItemInfoRemind =
                SharedItemInfoRemind(
                    data.getLong(KEY_TEAM_ID),
                    data.getString(KEY_TEAM_TITLE),
                    data.getString(KEY_ITEM_NAME),
                )
        }
    }

    data class AssignedItemInfoRemind(
        val teamBottariId: Long,
        val teamBottariTitle: String,
        val teamItemName: String,
        val publishedAt: String,
    ) : FcmMessage() {
        override fun sendNotification(notificationHelper: NotificationHelper) {
            notificationHelper.sendTeamMessage(
                teamBottariId,
                teamBottariTitle,
                R.string.notification_team_bottari_remind_by_item_message,
                teamItemName,
            )
        }

        companion object {
            const val TYPE = "ASSIGNED_ITEM_INFO_REMIND"
            private const val KEY_TEAM_ID = "teamBottariId"
            private const val KEY_TEAM_TITLE = "teamBottariTitle"
            private const val KEY_ITEM_NAME = "teamItemName"
            private const val KEY_PUBLISHED_AT = "publishedAt"

            fun fromData(data: JSONObject): AssignedItemInfoRemind =
                AssignedItemInfoRemind(
                    data.getLong(KEY_TEAM_ID),
                    data.getString(KEY_TEAM_TITLE),
                    data.getString(KEY_ITEM_NAME),
                    data.getString(KEY_PUBLISHED_AT),
                )
        }
    }

    data class TeamMemberRemind(
        val teamBottariId: Long,
        val teamBottariTitle: String,
        val teamSharedItemNames: List<String>,
        val teamAssignedItemNames: List<String>,
    ) : FcmMessage() {
        override fun sendNotification(notificationHelper: NotificationHelper) {
            notificationHelper.sendTeamMessage(
                teamBottariId,
                teamBottariTitle,
                R.string.notification_remind_by_member_message,
            )
        }

        companion object {
            const val TYPE = "TEAM_MEMBER_REMIND"
            private const val KEY_TEAM_ID = "teamBottariId"
            private const val KEY_TEAM_TITLE = "teamBottariTitle"

            fun fromData(data: JSONObject): TeamMemberRemind =
                TeamMemberRemind(
                    data.getLong(KEY_TEAM_ID),
                    data.getString(KEY_TEAM_TITLE),
                    data.getString("teamSharedItemNames").split(","),
                    data.getString("teamAssignedItemNames").split(","),
                )
        }
    }

    data class Unknown(
        val rawData: Map<String, String>,
    ) : FcmMessage() {
        override fun sendNotification(notificationHelper: NotificationHelper) {
            val eventType = rawData.getType()
            val dataSummary = rawData.entries.joinToString(", ") { "${it.key}=${it.value}" }
            BottariLogger.error("[FCM] Unknown type: $eventType, data: $dataSummary")
        }
    }

    companion object {
        const val KEY_RESOURCE = "resource"
        const val KEY_EVENT = "event"
        const val KEY_DATA = "data"

        fun fromData(data: Map<String, String>): FcmMessage {
            val eventData = data.getData() ?: return Unknown(data)
            return when (data.getType()) {
                TeamMemberDelete.TYPE -> TeamMemberDelete.fromData(eventData)
                AssignedItemInfoRemind.TYPE -> AssignedItemInfoRemind.fromData(eventData)
                SharedItemInfoRemind.TYPE -> SharedItemInfoRemind.fromData(eventData)
                TeamMemberRemind.TYPE -> TeamMemberRemind.fromData(eventData)
                else -> Unknown(data)
            }
        }
    }
}

private fun Map<String, String>.getType(): String = this[KEY_RESOURCE] + "_" + this[KEY_EVENT]

private fun Map<String, String>.getData(key: String = FcmMessage.KEY_DATA): JSONObject? = this[key]?.let { JSONObject(it) }

private fun NotificationHelper.sendTeamMessage(
    teamId: Long,
    teamTitle: String,
    messageRes: Int,
    vararg args: Any,
) {
    sendTeamNotification(teamId, teamTitle, getString(messageRes, *args))
}
