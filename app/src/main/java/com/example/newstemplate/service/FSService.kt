package com.example.newstemplate.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.opengl.Visibility
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_SECRET
import com.example.newstemplate.ForegroundServicesMainActivity
import com.example.newstemplate.R

class FSService : Service() {


    companion object {
        const val NOTIFICATION_ID = 1243
        const val CHANNEL_ID = "ffff"
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    private fun createNotification(): Notification {
        // Create notification channel if Android version is Oreo or higher
        createNotificationChannel()

        // Create custom RemoteViews for the notification layout
        val customNotificationLayout = RemoteViews(packageName, R.layout.notification_play_audio)
        customNotificationLayout.setTextViewText(R.id.notification_title, "Custom Foreground Service")
        customNotificationLayout.setTextViewText(R.id.notification_text, "Running...")


        // Build the notification
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Service is running")
            //.setSmallIcon(R.drawable.breaking_news_symbol)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // For pre-Oreo devices
            //.setSmallIcon(R.drawable.breaking_news_symbol)
            //.setCustomContentView(customNotificationLayout)
            //.setCustomBigContentView(customNotificationLayout)
            //.setAutoCancel(true)
            //.setPriority(0)
            //.setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId)
        //startForeground(NOTIFICATION_ID, createNotification())
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Foreground Service Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            //channel.lockscreenVisibility = VISIBILITY_SECRET
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}