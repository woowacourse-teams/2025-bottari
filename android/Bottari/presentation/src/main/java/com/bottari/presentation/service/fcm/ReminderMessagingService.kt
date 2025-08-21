package com.bottari.presentation.service.fcm

import android.content.Intent
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.fcm.SaveFcmTokenUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.R
import com.bottari.presentation.util.NotificationHelper
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ReminderMessagingService(
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase = UseCaseProvider.saveFcmTokenUseCase,
) : FirebaseMessagingService() {
    private val notificationHelper: NotificationHelper by lazy { NotificationHelper() }
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveFcmToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        BottariLogger.data("[FCM] onMessageReceived!\n${message.data}")
        if (message.data.isEmpty()) return
        handleMessageData(message.data)
    }

    override fun handleIntent(intent: Intent?) {
        val sanitizedIntent =
            intent?.apply {
                extras
                    ?.apply {
                        remove(Constants.MessageNotificationKeys.ENABLE_NOTIFICATION)
                        remove(NOTIFICATION_PREFIX_OLD)
                    }?.also { replaceExtras(it) }
            }
        super.handleIntent(sanitizedIntent)
    }

    private fun saveFcmToken(token: String) {
        serviceScope.launch {
            saveFcmTokenUseCase(token)
                .onFailure { error ->
                    BottariLogger.error(LOG_FORMAT.format(error.message), error)
                }
        }
    }

    private fun handleMessageData(data: Map<String, String>) {
        val teamBottariId = data[KEY_TEAM_BOTTARI_ID].toLongOrNullWithLog() ?: return
        val teamBottariTitle = data[KEY_TEAM_BOTTARI_TITLE].orNullIfBlank() ?: return
        val type = data[KEY_MESSAGE_TYPE].orNullIfBlank() ?: return

        when (type) {
            TYPE_TEAM_ITEM_CHANGED ->
                handleItemChangedMessage(teamBottariId, teamBottariTitle)

            TYPE_REMIND_BY_TEAM_MEMBER ->
                handleRemindByMemberMessage(teamBottariId, teamBottariTitle)

            TYPE_REMIND_BY_ITEM -> {
                val item = data[KEY_TEAM_ITEM_NAME].orEmpty()
                handleRemindByItemMessage(teamBottariId, teamBottariTitle, item)
            }

            TYPE_EXIT_TEAM_BOTTARI -> {
                val memberName = data[KEY_EXIT_MEMBER_NAME].orEmpty()
                handleExitTeamBottariMessage(teamBottariId, teamBottariTitle, memberName)
            }
        }
    }

    private fun String?.toLongOrNullWithLog(): Long? =
        runCatching { this?.toLong() }
            .onFailure { error ->
                BottariLogger.error(LOG_FORMAT.format(error.message), error)
            }.getOrNull()

    private fun String?.orNullIfBlank(): String? = this?.takeIf { it.isNotBlank() }

    private fun handleItemChangedMessage(
        id: Long,
        title: String,
    ) {
        val message = getString(R.string.common_team_bottari_notification_item_changed_message_text)
        notificationHelper.sendTeamNotification(id, title, message)
    }

    private fun handleRemindByMemberMessage(
        id: Long,
        title: String,
    ) {
        val message =
            getString(R.string.common_team_bottari_notification_send_remind_by_member_message_text)
        notificationHelper.sendTeamNotification(id, title, message)
    }

    private fun handleRemindByItemMessage(
        id: Long,
        title: String,
        item: String,
    ) {
        val message =
            getString(
                R.string.common_team_bottari_notification_send_remind_by_item_message_text,
                item,
            )
        notificationHelper.sendTeamNotification(id, title, message)
    }

    private fun handleExitTeamBottariMessage(
        id: Long,
        title: String,
        memberName: String,
    ) {
        val message =
            getString(
                R.string.common_team_bottari_notification_exit_team_bottari_message_text,
                memberName,
                title,
            )
        notificationHelper.sendTeamNotification(id, title, message)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.coroutineContext.cancel()
    }

    companion object {
        private const val LOG_FORMAT = "[FCM] %s"
        private const val NOTIFICATION_PREFIX_OLD = "gcm.notification"

        private const val KEY_TEAM_BOTTARI_ID = "teamBottariId"
        private const val KEY_TEAM_BOTTARI_TITLE = "teamBottariTitle"
        private const val KEY_TEAM_ITEM_NAME = "teamItemName"
        private const val KEY_EXIT_MEMBER_NAME = "exitMemberName"
        private const val KEY_MESSAGE_TYPE = "type"

        private const val TYPE_TEAM_ITEM_CHANGED = "TEAM_ITEM_CHANGED"
        private const val TYPE_REMIND_BY_TEAM_MEMBER = "REMIND_BY_TEAM_MEMBER"
        private const val TYPE_REMIND_BY_ITEM = "REMIND_BY_ITEM"
        private const val TYPE_EXIT_TEAM_BOTTARI = "EXIT_TEAM_BOTTARI"
    }
}
