package com.example.sampleapplication.core.domain.entities

import android.app.PendingIntent

data class NotificationData(
    val title: String,
    val message: String,
    val notificationId: Int,
    val channelData: ChannelData?
)