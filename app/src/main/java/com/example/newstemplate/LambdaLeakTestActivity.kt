package com.example.newstemplate

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.newstemplate.databinding.ActivityLambdaLeakTestBinding
import java.lang.ref.WeakReference
import java.util.Timer
import java.util.TimerTask


class LambdaLeakTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLambdaLeakTestBinding
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLambdaLeakTestBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.lambdaLeakTestBtn.setOnClickListener {
            //1. no leak
            //count++
            //binding.lambdaLeakTestBtn.text = ""+count

            //2. leak

            //Handler(Looper.getMainLooper()).postDelayed({
           //     binding.lambdaLeakTestBtn.text = ""+count
           // }, 10000)


            //3 lifecycleScope no leak
           // Handler(Looper.getMainLooper()).postDelayed({
          //      lifecycleScope.launch {
           //         binding.lambdaLeakTestBtn.text = ""+count
           //     }
          //  }, 10000)

            //4.no leak，在ondestory 加上取消listener
           // Handler(Looper.getMainLooper()).postDelayed({
           //     binding.lambdaLeakTestBtn.text = ""+count
           // }, 10000)

            //5
             //Handler(Looper.getMainLooper()).postDelayed({
                 //updatePost() //no leak
            //     update()//leak
            // }, 4000)

            //6. 注意 WeakReference 位置
            /*
            val activity = WeakReference(this@LambdaLeakTestActivity)

            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    activity.get()?.let {
                        Log.d("tag", "$it")
                    }
                }
            }, 0, 1000)


            Handler(Looper.getMainLooper()).postDelayed({
                activity.get()?.let {
                    Log.d("tag", "$it")
                }
             }, 4000)

             */
        }
    }

    //noleak
    private fun updatePost() {
        binding.lambdaLeakTestBtn.post{
            binding.lambdaLeakTestBtn.text = ""+count
        }
    }

    //leak
    private fun update() {
        binding.lambdaLeakTestBtn.text = ""+count
    }
    override fun onDestroy() {
        super.onDestroy()
        //4
        //binding.lambdaLeakTestBtn.setOnClickListener(null)

    }

}