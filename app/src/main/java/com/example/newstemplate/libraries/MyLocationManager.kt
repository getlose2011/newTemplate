package com.example.newstemplate.libraries

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface IMyObserver{
    fun sendData()
}

class MyObserver(val listner:IMyObserver) : DefaultLifecycleObserver {

    val TAG = "MyObserver"

    override fun onResume(owner: LifecycleOwner) {
        Log.d(TAG, "onResume: ")
        //listner.sendData()
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d(TAG, "onPause: ")
    }


}
