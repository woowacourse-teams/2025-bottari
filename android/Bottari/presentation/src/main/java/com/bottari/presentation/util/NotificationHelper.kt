package com.bottari.presentation.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.bottari.di.ApplicationContextProvider
import com.bottari.presentation.R
import com.bottari.presentation.view.checklist.personal.ChecklistActivity
import com.bottari.presentation.view.checklist.team.TeamChecklistActivity

class NotificationHelper(
    private val context: Context = ApplicationContextProvider.applicationContext,
) {
    private val manager: NotificationManager =
        context.getSystemService(NotificationManager::class.java)

    fun sendPersonalNotification(
        bottariId: Long,
        bottariTitle: String,
    ) {
        createPersonalNotificationChannel()
        val pendingIntent = createPersonalPendingIntent(bottariId, bottariTitle)
        val notification = createPersonalNotification(pendingIntent, bottariTitle)
        manager.notify(bottariId.toInt(), notification)
    }

    fun sendTeamNotification(
        teamBottariId: Long,
        teamBottariTitle: String,
        message: String,
    ) {
        createTeamNotificationChannel()
        val pendingIntent = createTeamPendingIntent(teamBottariId, teamBottariTitle)
        val notification = createTeamNotification(pendingIntent, teamBottariTitle, message)
        manager.notify(teamBottariId.toInt(), notification)
    }

    private fun createPersonalNotificationChannel() {
        val channel = createBottariNotificationChannel(BOTTARI_CHANNEL_ID)
        manager.createNotificationChannel(channel)
    }

    private fun createTeamNotificationChannel() {
        val channel = createBottariNotificationChannel(TEAM_BOTTARI_CHANNEL_ID)
        manager.createNotificationChannel(channel)
    }

    private fun createPersonalPendingIntent(
        bottariId: Long,
        bottariTitle: String,
    ): PendingIntent {
        val intent =
            ChecklistActivity.newIntentForNotification(
                context,
                bottariId,
                bottariTitle,
            )
        return PendingIntent.getActivity(
            context,
            bottariId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun createPersonalNotification(
        intent: PendingIntent,
        bottariTitle: String,
    ): Notification =
        NotificationCompat
            .Builder(context, BOTTARI_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bottari_logo)
            .setContentTitle(
                context.getString(R.string.common_bottari_notification_title, bottariTitle),
            ).setContentText(context.getString(R.string.common_bottari_notification_message))
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

    private fun createTeamPendingIntent(
        teamBottariId: Long,
        teamBottariTitle: String,
    ): PendingIntent {
        val intent =
            TeamChecklistActivity.newIntentForNotification(
                context,
                teamBottariId,
                teamBottariTitle,
            )
        return PendingIntent.getActivity(
            context,
            teamBottariId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun createTeamNotification(
        intent: PendingIntent,
        bottariTitle: String,
        message: String,
    ): Notification =
        NotificationCompat
            .Builder(context, TEAM_BOTTARI_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bottari_logo)
            .setContentTitle(context.getString(R.string.common_team_bottari_notification_title_text, bottariTitle))
            .setContentText(message)
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

    private fun createBottariNotificationChannel(channelId: String): NotificationChannel {
        val audioAttributes = createAudioAttributes()
        return NotificationChannel(
            channelId,
            context.getString(R.string.common_bottari_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            enableLights(true)
            enableVibration(true)
            vibrationPattern = VIBRATION_PATTERN
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                audioAttributes,
            )
        }
    }

    private fun createAudioAttributes(): AudioAttributes =
        AudioAttributes
            .Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

    companion object {
        private const val BOTTARI_CHANNEL_ID = "BOTTARI_CHANNEL_ID"
        private const val TEAM_BOTTARI_CHANNEL_ID = "TEAM_BOTTARI_CHANNEL_ID"
        private val VIBRATION_PATTERN = longArrayOf(0, 300, 200, 300)
    }
}
