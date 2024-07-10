package com.example.newstemplate

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.newstemplate.databinding.ActivityForegroundServicesMainBinding
import com.example.newstemplate.service.FSService


class ForegroundServicesMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForegroundServicesMainBinding
    private val REQUEST_FOREGROUND_SERVICE = 1
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 權限授予後顯示通知
                starts()
            } else {
                // 權限被拒絕
                Toast.makeText(this, "通知權限被拒絕", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForegroundServicesMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // 檢查並請求通知權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.FOREGROUND_SERVICE) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // 權限已授予，顯示通知
                starts()
            } else {
                // 請求權限
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // Android 13 以下版本直接顯示通知
            starts()
        }

    }


    fun starts(){

        val serviceIntent = Intent(this, FSService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

}