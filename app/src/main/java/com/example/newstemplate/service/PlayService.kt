package com.example.newstemplate.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.newstemplate.PlayAudioActivity

class PlayService : Service() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var i = 1
    private var isRunning = false
    lateinit var localBinder:PlayService.LocalBinder

    inner class LocalBinder: Binder(){
        val service : PlayService
            get() = this@PlayService
    }

    override fun onBind(i:Intent?):IBinder?{
        TODO("not")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("PlayService", "Service Created")

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                Log.d("PlayService", "Running task...")
                // 每隔一段時間執行的任務
                sendBroadcastUpdate("Task running $i")
                i++
                handler.postDelayed(this, 5000) // 5 秒
            }
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            Log.d("PlayService", "Service Started")

            // 取得從 Intent 傳遞過來的數據
            val data = intent?.getStringExtra("data") ?: "No data"
            Log.d("PlayService", "Data received: $data")
            // 在這裡開始你的任務
            handler.post(runnable)
        } else {
            Log.d("PlayService", "Service is already running")
        }
        // 在此執行服務的任務，例如網路請求或資料處理

        // 如果希望服務在執行完畢後自動停止，可以在這裡呼叫 stopSelf()
        // stopSelf()

       // Log.d("PlayService", "Service Started")



        // 開始定時任務
       // handler.post(runnable)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PlayService", "Service Destroyed")
    }

    //override fun onBind(intent: Intent?): IBinder? {
        // 我們不打算使用綁定服務，所以返回 null
   //     return null
   // }

    val serviceConnect= object:ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            localBinder = service as PlayService.LocalBinder
            localBinder.service.isRunning
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    private fun sendBroadcastUpdate(message: String) {
        val intent = Intent(PlayAudioActivity.PLAY_RECEIVE_CODE_Intent_Filter)
        intent.putExtra("message", message)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}