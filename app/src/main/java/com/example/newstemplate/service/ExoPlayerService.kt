package com.example.newstemplate.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.newstemplate.R
import com.example.newstemplate.Receiver.ExoPlayerReceiver


class ExoPlayerService : Service() {



    private lateinit var customNotificationLayout: RemoteViews
    private lateinit var notificationManager: NotificationManager
    private val NOTIFICATION_ID = 12435
    private val CHANNEL_ID = "exoplayer_service"
    private val defaultTime = "00:00"

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    @SuppressLint("RemoteViewLayout")
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        customNotificationLayout = RemoteViews(packageName, R.layout.exo_player_remote_view)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        initNotificationLayout()
        startForeground(NOTIFICATION_ID, createNotification())
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            "CURRENT_POSITION" -> {
                // 取得從 Intent 傳遞過來的數據
                val data = intent?.getStringExtra("data") ?: defaultTime
                updateNotification(data)
            }
            "DELETE" -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        when(intent?.action){
            "current" -> {
                //updateNotification(intent?.info)
            }
        }


        return START_STICKY//super.onStartCommand(intent, flags, startId)
    }

    private fun initNotificationLayout() {
        customNotificationLayout.setTextViewText(R.id.exo_remote_current, defaultTime)
        customNotificationLayout.setTextViewText(R.id.exo_remote_duration, defaultTime)

        // 设置点击事件的Intent和PendingIntent
        // 设置点击事件的Intent和PendingIntent
        val nextIntent = Intent(this, ExoPlayerReceiver::class.java)
        nextIntent.action = ExoPlayerReceiver.ACTION
        val flags =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else 0
        val nextPendingIntent =
            PendingIntent.getBroadcast(this, 0, nextIntent, flags)
        customNotificationLayout.setOnClickPendingIntent(R.id.exo_remote_next, nextPendingIntent)
        //customNotificationLayout.setOnClickPendingIntent(androidx.media3.ui.R.id.exo_next, getPendingIntent("NEXT"))
        //customNotificationLayout.setOnClickPendingIntent(androidx.media3.ui.R.id.exo_prev, getPendingIntent("PREV"))
    }

    private fun updateNotification(d:String) {
        customNotificationLayout.setTextViewText(R.id.exo_remote_current, d)
        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "ExoPlayer Service Channel"
            val importance = NotificationManager.IMPORTANCE_LOW//改成IMPORTANCE_LOW避免一直震動
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
            //.setVibrate(longArrayOf(0, 100))沒機器試
            .build()
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, FSService::class.java).apply { this.action = action }
        return PendingIntent.getService(this, action.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun nextPlay() {
        //val broadcastIntent = Intent(ACTION_CURRENT_POSITION)
        //broadcastIntent.putExtra("data", "next")
        //LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
}