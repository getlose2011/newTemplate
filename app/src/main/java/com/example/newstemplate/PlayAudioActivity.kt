package com.example.newstemplate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newstemplate.databinding.ActivityPlayAudioBinding
import com.example.newstemplate.service.NotificationService


class PlayAudioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayAudioBinding

    private var myService: NotificationService? = null
    private var isBound = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind to the service
        // Bind to the service
        //Intent(this, NotificationService::class.java)
        val serviceIntent = Intent(this, NotificationService::class.java)
        startService(serviceIntent)

        binding.playAudioBtn.setOnClickListener {

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            //unbindService(serviceConnection)
            isBound = false
        }
    }


}