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

class NotificationHelper(
    private val context: Context = ApplicationContextProvider.applicationContext,
) {
    private val manager: NotificationManager =
        context.getSystemService(NotificationManager::class.java)

    fun sendNotification(
        bottariId: Long,
        bottariTitle: String,
    ) {
        createNotificationChannel()
        val pendingIntent = createClickIntent(bottariId, bottariTitle)
        val notification = createNotification(pendingIntent, bottariTitle)
        manager.notify(bottariTitle.hashCode(), notification)
    }

    private fun createNotificationChannel() {
        val audioAttributes = createAudioAttributes()
        val channel = createBottariNotificationChannel(audioAttributes)
        manager.createNotificationChannel(channel)
    }

    private fun createClickIntent(
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
            bottariTitle.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun createNotification(
        intent: PendingIntent,
        bottariTitle: String,
    ): Notification =
        NotificationCompat
            .Builder(context, BOTTARI_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bottari_logo)
            .setContentTitle(
                context.getString(R.string.common_bottari_notification_title).format(bottariTitle),
            ).setContentText(context.getString(R.string.common_bottari_notification_message))
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

    private fun createAudioAttributes(): AudioAttributes =
        AudioAttributes
            .Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

    private fun createBottariNotificationChannel(audioAttributes: AudioAttributes): NotificationChannel =
        NotificationChannel(
            BOTTARI_CHANNEL_ID,
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

    companion object {
        private const val BOTTARI_CHANNEL_ID = "BOTTARI_CHANNEL_ID"
        private val VIBRATION_PATTERN = longArrayOf(0, 300, 200, 300)
    }
}
