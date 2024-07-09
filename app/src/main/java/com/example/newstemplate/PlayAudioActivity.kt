package com.example.newstemplate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.newstemplate.databinding.ActivityPlayAudioBinding
import com.example.newstemplate.service.PlayService


class PlayAudioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayAudioBinding
    private var isBound = false


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
        }

        binding.stopAudioBtn.setOnClickListener {
            val intent = Intent(this, PlayService::class.java)
            stopService(intent)
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(PLAY_RECEIVE_CODE_Intent_Filter))
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message = intent?.getStringExtra("message") ?: "No message"
            binding.playAudioTxt.text = message
        }
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