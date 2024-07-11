package com.example.newstemplate.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.newstemplate.R
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class FSService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1243
        const val CHANNEL_ID = "foreground_service"
        var isRunnig = false
    }

    private lateinit var customNotificationLayout: RemoteViews
    private lateinit var notificationManager: NotificationManager
    private lateinit var scheduler: ScheduledExecutorService
    private var counter = 0

    override fun onCreate() {
        super.onCreate()
        isRunnig = true
        createNotificationChannel()
        customNotificationLayout = RemoteViews(packageName, R.layout.notification_play_audio)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        initNotificationLayout()
        startForeground(NOTIFICATION_ID, createNotification())
        scheduler = Executors.newScheduledThreadPool(1)
        startNotificationUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TAG", "onStartCommand: ${intent?.action}")

        when (intent?.action) {
            "BUTTON_1_CLICKED" -> {
                // Button 1 clicked action
            }
            "DELETE" -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }


    private fun initNotificationLayout() {
        customNotificationLayout.setTextViewText(R.id.notification_title, "new template")
        customNotificationLayout.setTextViewText(R.id.notification_text, "media player")

        customNotificationLayout.setOnClickPendingIntent(R.id.button1, getPendingIntent("BUTTON_1_CLICKED"))
        customNotificationLayout.setOnClickPendingIntent(R.id.button2, getPendingIntent("DELETE"))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Foreground Service Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.breaking_news_symbol)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCustomContentView(customNotificationLayout)
            .setCustomBigContentView(customNotificationLayout)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setDeleteIntent(getPendingIntent("DELETE"))
            .build()
    }


    private fun startNotificationUpdates() {
        scheduler.scheduleAtFixedRate({
            updateNotification()
            counter++
        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun updateNotification() {
        customNotificationLayout.setTextViewText(R.id.notification_text, "Updated text: $counter")
        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }


    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, FSService::class.java).apply { this.action = action }
        return PendingIntent.getService(this, action.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunnig = false
        scheduler.shutdown()
        Log.d("TAG", "FSService onDestroy")
    }
}
