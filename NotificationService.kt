package com.example.empirehouseproduction.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.empirehouseproductions.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {
    
    override fun onNewToken(token: String) {
        // Send token to your server if needed
        println("FCM Token: $token")
    }
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages here
        remoteMessage.notification?.let { notification ->
            sendNotification(notification.title ?: "Empire House", notification.body ?: "")
        }
    }
    
    private fun sendNotification(title: String, message: String) {
        createNotificationChannel()
        
        val builder = NotificationCompat.Builder(this, "empire_house_channel")
            .setSmallIcon(R.drawable.ic_notification) // You'll need to add this icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        
        with(NotificationManagerCompat.from(this)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "empire_house_channel",
                "Empire House Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for booking updates and system alerts"
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}