package com.example.sampleapplication.core.presentation.helpers

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.sampleapplication.R
import com.example.sampleapplication.core.domain.entities.ChannelData
import com.example.sampleapplication.core.domain.entities.NotificationData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class NotificationHelper @Inject constructor(@ApplicationContext private val context: Context) {
    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    fun requestPermission() {
    }

    fun showNotification(data: NotificationData) {
        val notificationBuilder = buildNotification(data)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                context, context.getString(R.string.please_grant_notification_permission),
                Toast.LENGTH_SHORT
            )
                .show()
            return
        }

        notificationManager.notify(data.notificationId, notificationBuilder.build())
    }

    private fun buildNotification(data: NotificationData): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (data.channelData != null) {

                val notificationChannel = NotificationChannel(
                    data.channelData.channelId,
                    data.channelData.name,
                    NotificationManager.IMPORTANCE_LOW
                )

                // Configure the notification channel.
                notificationChannel.description = data.channelData.description
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        return NotificationCompat.Builder(context, data.channelData?.channelId ?: "default")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(data.title)
            .setContentText(data.message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
    }

}