package com.example.newstemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newstemplate.libraries.IMyObserver
import com.example.newstemplate.libraries.MyObserver

open class BaseActivity : AppCompatActivity() {

    private val myObserver by lazy {
        MyObserver(object: IMyObserver {
            override fun sendData() {
                Toast.makeText(this@BaseActivity,"f", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(myObserver)
    }
}