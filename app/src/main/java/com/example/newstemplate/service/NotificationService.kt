package com.example.newstemplate.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.newstemplate.MainActivity
import com.example.newstemplate.PlayAudioActivity
import com.example.newstemplate.R
import java.util.concurrent.atomic.AtomicInteger

private const val CHANNEL_ID = "my_service_channel"
private const val NOTIFICATION_ID = 123

class NotificationService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Show a notification when service starts
        showNotification()

        // Return START_STICKY to ensure the service keeps running
        return START_STICKY
    }

    private fun showNotification() {
        val notificationId = 1
        val channelId = "my_channel_id"
        val notificationIntent = Intent(this, PlayAudioActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.bg_bottom_sheet)
            .setContentTitle("My Service Notification")
            .setContentText("MyService is running...")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the Notification Channel if API level is 26 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}