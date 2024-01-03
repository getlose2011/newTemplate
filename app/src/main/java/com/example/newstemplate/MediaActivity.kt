package com.example.newstemplate


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newstemplate.databinding.ActivityMediaBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MediaActivity() : AppCompatActivity() {

    private val TAG = "MediaActivity"
    private lateinit var binding: ActivityMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch{
            delay(2000)

            withContext(Dispatchers.Main) {
                Picasso.with(this@MediaActivity)
                    .load("https://obs.line-scdn.net/0hYl9c3pXNBkMEGhWwCKx5FDxMCjI3fBxKJnxIISAYDyYhNkNFO3pVICBPCm96KEIVJHkZLSMbC3ssf0ATMA/w280")
                    .fit()
                    .centerInside()
                    .placeholder(R.mipmap.image_default)
                    .error(R.mipmap.image_default)
                    .into(binding.imageIv)
            }


        }



    }


}