package com.example.newstemplate

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.newstemplate.databinding.ActivityPlayAudioBinding
import com.example.newstemplate.service.PlayService


class PlayAudioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayAudioBinding
    private var isBound = false
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 權限授予後顯示通知
                showCustomNotification(this)
            } else {
                // 權限被拒絕
                Toast.makeText(this, "通知權限被拒絕", Toast.LENGTH_SHORT).show()
            }
        }

    companion object{
        val PLAY_RECEIVE_CODE_Intent_Filter = "PLAY_RECEIVE_CODE_Intent_Filter"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playAudioBtn.setOnClickListener {
            val intent = Intent(this, PlayService::class.java)
            intent.putExtra("data", "Some important data")
            startService(intent)
            showCustomNotification(this)
        }

        binding.stopAudioBtn.setOnClickListener {
            //val intent = Intent(this, PlayService::class.java)
            //stopService(intent)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(12)
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(PLAY_RECEIVE_CODE_Intent_Filter))

        // 創建通知渠道
        createNotificationChannel(this)
        showCustomNotification(this)

        // 檢查是否由動作按鈕觸發
        //if (intent?.action == "ACTION_BUTTON_CLICKED") {
       //     Toast.makeText(this, "動作按鈕被點擊", Toast.LENGTH_SHORT).show()
        //}

        // 檢查是否由動作按鈕觸發
        when (intent?.action) {
            "BUTTON_1_CLICKED" -> {
                Toast.makeText(this, "按鈕1被點擊", Toast.LENGTH_SHORT).show()
            }
            "BUTTON_2_CLICKED" -> {
                Toast.makeText(this, "按鈕2被點擊", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showNotification(){


        // 檢查並請求通知權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // 權限已授予，顯示通知
                showCustomNotification(this)
            } else {
                // 請求權限
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // Android 13 以下版本直接顯示通知
            showCustomNotification(this)
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message = intent?.getStringExtra("message") ?: "No message"
            binding.playAudioTxt.text = message
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "example_channel_id"
            val channelName = "Example Channel"
            val channelDescription = "This is an example notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context) {

        // 檢查是否具有通知權限（針對 Android 13 及以上版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // 如果沒有權限，不顯示通知
                return
            }
        }

        val channelId = "example_channel_id"

        // 創建 PendingIntent，以便在點擊通知時打開 PlayAudioActivity
        val intent = Intent(context, PlayAudioActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        // 創建動作按鈕的 PendingIntent
        val actionIntent = Intent(context, PlayAudioActivity::class.java).apply {
            action = "ACTION_BUTTON_CLICKED"
        }
        val actionPendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.bg_bottom_sheet) // 替換為你的圖標
            .setContentTitle("範例通知")
            .setContentText("這是一個簡單的通知示例")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.play, "play", actionPendingIntent) // 添加動作按鈕

        with(NotificationManagerCompat.from(context)) {
            // 顯示通知，通知 ID 需為唯一值
            notify(1, builder.build())
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun showCustomNotification(context: Context) {
        // 檢查是否具有通知權限（針對 Android 13 及以上版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // 如果沒有權限，不顯示通知
                return
            }
        }

        val channelId = "example_channel_id"

        // 創建 PendingIntent，以便在點擊通知時打開 PlayAudioActivity
        val intent = Intent(context, PlayAudioActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // 創建自定義通知佈局
        val notificationLayout = RemoteViews(packageName, R.layout.notification_play_audio)
        notificationLayout.setTextViewText(R.id.notification_title, "自定義通知標題")
        notificationLayout.setTextViewText(R.id.notification_text, "這是自定義通知內容")

        // 設置按鈕點擊事件
        val button1Intent = Intent(context, PlayAudioActivity::class.java).apply {
            action = "BUTTON_1_CLICKED"
        }
        val button1PendingIntent: PendingIntent = PendingIntent.getActivity(context, 1, button1Intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        notificationLayout.setOnClickPendingIntent(R.id.button1, button1PendingIntent)

        val button2Intent = Intent(context, PlayAudioActivity::class.java).apply {
            action = "BUTTON_2_CLICKED"
        }
        val button2PendingIntent: PendingIntent = PendingIntent.getActivity(context, 2, button2Intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        notificationLayout.setOnClickPendingIntent(R.id.button2, button2PendingIntent)


        // service 設置按鈕點擊事件
        val button3Intent = Intent(context, PlayService::class.java).apply {
            action = "BUTTON_3_CLICKED"
        }
        val button3PendingIntent: PendingIntent = PendingIntent.getService(context, 1, button3Intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        notificationLayout.setOnClickPendingIntent(R.id.button3, button3PendingIntent)

        // 創建 PendingIntent，以便在點擊通知時打開 PlayAudioActivity
        val deleteIntent = Intent(context, PlayAudioActivity::class.java).apply {
            action = "DELETE"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val deletePendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.breaking_news_symbol) // 替換為你的圖標
            //.setContent(notificationLayout) // 設置自定義通知佈局
            //.setContentIntent(pendingIntent)
            //.setAutoCancel(true)
            //.setStyle(NotificationCompat.DecoratedCustomViewStyle())
           // .setDeleteIntent(deletePendingIntent)
            .setContentTitle("title")
            .setContentText("body")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(12,builder)

    }


    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            //unbindService(serviceConnection)
            isBound = false
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

}