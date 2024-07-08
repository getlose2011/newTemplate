package com.example.newstemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoroutineActivity : AppCompatActivity() {
    val TAG = "CoroutineActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)
        var name = Thread.currentThread().name
        /**
         *非同步取得資料，適用一筆一筆
         *可以比較api時間長短、位置取得的資料時間會時間上的差異
         *
        getdata()
        getdata1()
         */

        /**
         *非同步取得多筆資料
         *可以比較api時間長短、位置取得的資料時間會時間上的差異
         *
        getdata2()
        getdata3()
        getdata4()
         */
    }

    /**
     * 非同步會同時要資料，適用一筆
     * 按照step 1,2,3 順序
     * 2024-01-11 17:47:24.337  getdata4_1
     * 2024-01-11 17:47:39.361  getdata4_2 Hello
     * 2024-01-11 17:47:39.362  getdata4_3 World
     * 2024-01-11 17:47:39.362  getdata4_4
     * */
    private fun getData() {
        //取得資料
        //
        GlobalScope.launch(Dispatchers.Main) {
            //main step 1
            Log.d(TAG, "getdata4_1")
            withContext(Dispatchers.IO) {
                // not main thread step 2
                val data = sayHello()
                val data1 = sayWorld()
                Log.d(TAG, "getdata4_2 $data")
                Log.d(TAG, "getdata4_3 $data1")
            }
            //main step 3
            Log.d(TAG, "getdata4_4")
        }

    }

    /**
     * 非同步會同時要資料，適用一筆
     * 按照step 1,2,3 順序
     * 2024-01-11 17:49:22.774 getdata4_1
     * 2024-01-11 17:49:27.778 getdata4_2 Hello
     * 2024-01-11 17:49:37.781 getdata4_3 World
     * 2024-01-11 17:49:37.782 getdata4_4
     * */
    private fun getData1() {
        //取得資料
        //
        GlobalScope.launch(Dispatchers.Main) {
            //main step 1
            Log.d(TAG, "getdata4_1")
            withContext(Dispatchers.IO) {
                // not main thread step 2
                val data = sayHello()
                Log.d(TAG, "getdata4_2 $data")
                val data1 = sayWorld()
                Log.d(TAG, "getdata4_3 $data1")
            }
            //main step 3
            Log.d(TAG, "getdata4_4")
        }

    }

    /**
     * 先要時間長的,再要時間短的
     * 非同步要資料,可同時多筆存取,適用多筆
     * 2024-01-11 17:34:55.935 getdata4_1
     * 2024-01-11 17:35:05.958 getdata4_2: World
     * 2024-01-11 17:35:05.959 getdata4_3: Hello
     * */
    fun getdata2(){
        GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "getdata4_1")

            val rawText: Deferred<String> = GlobalScope.async(Dispatchers.IO) {
                //not ui
                sayHello()
            }

            val rawText1: Deferred<String> = GlobalScope.async(Dispatchers.IO) {
                //not ui
                sayWorld()
            }
            //main
            // 等 rawText1
            val deferredResult1 = rawText1.await()
            Log.d(TAG, "getdata4_2: $deferredResult1")
            // 等 rawText
            val deferredResult = rawText.await()
            Log.d(TAG, "getdata4_3: $deferredResult")
        }

    }

    /**
     *同時要時間長和時間短的
     * 2024-01-11 17:36:18.618 getdata4_1
     * 2024-01-11 17:36:28.639 getdata4_2: Hello
     * 2024-01-11 17:36:28.639 getdata4_3: World
     *
     * */
    fun getdata3() {
        GlobalScope.launch(Dispatchers.Main) {
            // main

            Log.d(TAG, "getdata4_1")

            val rawText: Deferred<String> = GlobalScope.async(Dispatchers.IO) {
                //not ui
                sayHello()
            }

            val rawText1: Deferred<String> = GlobalScope.async(Dispatchers.IO) {
                //not ui
                sayWorld()
            }
            //main
            // 等 rawText, rawText1
            val deferredResult = rawText.await()
            val deferredResult1 = rawText1.await()

            Log.d(TAG, "getdata4_2: $deferredResult")
            Log.d(TAG, "getdata4_3: $deferredResult1")
        }

    }

    /**
     *先要時間短的,再要時間長的
     * 2024-01-11 17:37:35.310 getdata4_1
     * 2024-01-11 17:37:40.330 Hello
     * 2024-01-11 17:37:45.330 World
     * */
    fun getdata4(){
        GlobalScope.launch(Dispatchers.Main) {

            // main
            Log.d(TAG, "getdata4_1")

            val rawText: Deferred<String> = GlobalScope.async(Dispatchers.IO) {
                //not ui
                sayHello()
            }

            val rawText1: Deferred<String> = GlobalScope.async(Dispatchers.IO) {
                //not ui
                sayWorld()
            }
            //main
            val deferredResult = rawText.await()
            Log.d(TAG, "getdata4_2: $deferredResult")
            val deferredResult1 = rawText1.await()
            Log.d(TAG, "getdata4_3: $deferredResult1")
        }
    }

    private suspend fun sayHello(): String {
        delay(5000L)
        return "Hello"
    }

    private suspend fun sayWorld(): String {
        delay(10000L)
        return "World"
    }

}