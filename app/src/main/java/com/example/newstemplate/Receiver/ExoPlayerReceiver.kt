package com.example.newstemplate.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.newstemplate.service.ExoPlayerService


open class ExoPlayerReceiver : BroadcastReceiver() {

    companion object{
        val ACTION = "ExoPlayerReceiverAction"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // 处理下一首按钮的点击事件
        val broadcastIntent = Intent(ACTION)
        broadcastIntent.putExtra("data", "next")
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent)
    }
}