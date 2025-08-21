package com.bottari.presentation.service.fcm

import com.bottari.logger.BottariLogger
import com.bottari.presentation.R
import com.bottari.presentation.util.NotificationHelper

sealed class FcmMessage {
    abstract fun sendNotification(notificationHelper: NotificationHelper)

    data class TeamItemChanged(
        val teamBottariId: Long,
        val teamBottariTitle: String,
    ) : FcmMessage() {
        override fun sendNotification(notificationHelper: NotificationHelper) {
            notificationHelper.sendTeamMessage(
                teamBottariId,
                teamBottariTitle,
                R.string.common_team_bottari_notification_item_changed_message_text,
            )
        }

        companion object {
            const val TYPE = "TEAM_ITEM_CHANGED"

            fun fromData(data: Map<String, String>) =
                data.getLong(KEY_TEAM_ID)?.let { id ->
                    TeamItemChanged(id, data.getString(KEY_TEAM_TITLE))
                }
        }
    }

    data class RemindByMember(
        val teamBottariId: Long,
        val teamBottariTitle: String,
    ) : FcmMessage() {
        override fun sendNotification(notificationHelper: NotificationHelper) {
            notificationHelper.sendTeamMessage(
                teamBottariId,
                teamBottariTitle,
                R.string.common_team_bottari_notification_send_remind_by_member_message_text,
            )
        }

        companion object {
            const val TYPE = "REMIND_BY_TEAM_MEMBER"

            fun fromData(data: Map<String, String>) =
                data.getLong(KEY_TEAM_ID)?.let { id ->
                    RemindByMember(id, data.getString(KEY_TEAM_TITLE))
                }
        }
    }

    data class RemindByItem(
        val teamBottariId: Long,
        val teamBottariTitle: String,
        val itemName: String,
    ) : FcmMessage() {
        override fun sendNotification(notificationHelper: NotificationHelper) {
            notificationHelper.sendTeamMessage(
                teamBottariId,
                teamBottariTitle,
                R.string.common_team_bottari_notification_send_remind_by_item_message_text,
                itemName,
                teamBottariTitle,
            )
        }

        companion object {
            const val TYPE = "REMIND_BY_ITEM"
            private const val KEY_ITEM_NAME = "teamItemName"

            fun fromData(data: Map<String, String>) =
                data.getLong(KEY_TEAM_ID)?.let { id ->
                    RemindByItem(
                        id,
                        data.getString(KEY_TEAM_TITLE),
                        data.getString(KEY_ITEM_NAME),
                    )
                }
        }
    }

    data class ExitTeam(
        val teamBottariId: Long,
        val teamBottariTitle: String,
        val memberName: String,
    ) : FcmMessage() {
        override fun sendNotification(notificationHelper: NotificationHelper) {
            notificationHelper.sendTeamMessage(
                teamBottariId,
                teamBottariTitle,
                R.string.common_team_bottari_notification_exit_team_bottari_message_text,
                memberName,
                teamBottariTitle,
            )
        }

        companion object {
            const val TYPE = "EXIT_TEAM_BOTTARI"
            private const val KEY_MEMBER_NAME = "exitMemberName"

            fun fromData(data: Map<String, String>) =
                data.getLong(KEY_TEAM_ID)?.let { id ->
                    ExitTeam(
                        id,
                        data.getString(KEY_TEAM_TITLE),
                        data.getString(KEY_MEMBER_NAME),
                    )
                }
        }
    }

    data class Unknown(
        val rawData: Map<String, String>,
    ) : FcmMessage() {
        override fun sendNotification(notificationHelper: NotificationHelper) {
            val type = rawData[KEY_TYPE].orEmpty()
            val dataSummary = rawData.entries.joinToString(", ") { "${it.key}=${it.value}" }
            BottariLogger.error("[FCM] Unknown type: $type, data: $dataSummary")
        }
    }

    companion object {
        const val KEY_TYPE = "type"
        const val KEY_TEAM_ID = "teamBottariId"
        const val KEY_TEAM_TITLE = "teamBottariTitle"

        fun fromData(data: Map<String, String>): FcmMessage =
            when (data[KEY_TYPE]) {
                TeamItemChanged.TYPE -> TeamItemChanged.fromData(data) ?: Unknown(data)
                RemindByMember.TYPE -> RemindByMember.fromData(data) ?: Unknown(data)
                RemindByItem.TYPE -> RemindByItem.fromData(data) ?: Unknown(data)
                ExitTeam.TYPE -> ExitTeam.fromData(data) ?: Unknown(data)
                else -> Unknown(data)
            }
    }
}

private fun Map<String, String>.getLong(key: String): Long? = this[key]?.toLongOrNull()

private fun Map<String, String>.getString(key: String): String = this[key].orEmpty()

private fun NotificationHelper.sendTeamMessage(
    teamId: Long,
    teamTitle: String,
    messageRes: Int,
    vararg args: Any,
) {
    sendTeamNotification(teamId, teamTitle, getString(messageRes, *args))
}
