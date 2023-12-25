package com.example.newstemplate

import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newstemplate.databinding.ActivityCanvasBinding


class CanvasActivity : AppCompatActivity() {
    private val TAG = "CanvasActivity"
    private lateinit var binding: ActivityCanvasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCanvasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.canvasMyButton.setOnClickListener {
            binding.canvasMyView.changeRectangleColor()
        }
    }

}