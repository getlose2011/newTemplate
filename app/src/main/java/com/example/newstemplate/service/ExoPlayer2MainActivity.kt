package com.example.newstemplate.service

import android.app.Notification
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newstemplate.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import androidx.media3.ui.PlayerNotificationManager
import androidx.media3.session.MediaSessionService

class ExoPlayer2MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession
    private lateinit var playerNotificationManager: PlayerNotificationManager


    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player2_main)
        // 創建通知頻道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "playback_channel",
                "Playback Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // 初始化 ExoPlayer
        player = ExoPlayer.Builder(this).build()

        // 設置媒體項目
        //val mediaItem = MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")

        val data = arrayListOf<MediaItem>(MediaItem.fromUri("https://img.news.ebc.net.tw/EbcNews/audio/432113.mp3"),MediaItem.fromUri("https://img.news.ebc.net.tw/EbcNews/audio/432112.mp3"))
        //player.setMediaItem(mediaItem)
        player.setMediaItems(data)

        // 初始化 MediaSession
        mediaSession = MediaSession.Builder(this, player).build()

        // 准備播放器
        player.prepare()
        player.play()

        // 初始化 PlayerNotificationManager
        playerNotificationManager = PlayerNotificationManager.Builder(
            this,
            1,
            "playback_channel"
        )
            .setMediaDescriptionAdapter(MediaDescriptionAdapter(this))
            .setNotificationListener(NotificationListener(this))
            .setSmallIconResourceId(R.drawable.breaking_news_symbol)
            .build()

        playerNotificationManager.setPlayer(player)
        playerNotificationManager.setMediaSessionToken(mediaSession.sessionCompatToken)

    }

    @OptIn(UnstableApi::class) override fun onDestroy() {
        super.onDestroy()
        playerNotificationManager.setPlayer(null)
        player.release()
        mediaSession.release()
    }

    @UnstableApi private class MediaDescriptionAdapter(private val context: Context) : PlayerNotificationManager.MediaDescriptionAdapter {


        override fun getCurrentContentTitle(player: Player): CharSequence {
            return ""
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return null
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return null
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return null
        }
    }

    @UnstableApi private class NotificationListener(private val context: Context) : PlayerNotificationManager.NotificationListener {
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            // 處理通知取消事件
        }

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {

        }
    }
}