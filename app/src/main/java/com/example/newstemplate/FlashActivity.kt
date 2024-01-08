package com.example.newstemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newstemplate.databinding.ActivityFlashBinding


class FlashActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityFlashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /*
        binding.newsListFlashLinearLayout.post{
            val h = binding.newsListFlashLinearLayout.height

            binding.breakingArrowLottie.layoutParams.apply {
                val w = width
                height = h
                width = width
            }
        }
*/


    }
}