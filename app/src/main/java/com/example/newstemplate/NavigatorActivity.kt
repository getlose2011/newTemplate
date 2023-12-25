package com.example.newstemplate

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.example.newstemplate.databinding.ActivityNavigatorBinding
import com.example.newstemplate.fragment.NavigatorFragment
import kotlin.math.log


class NavigatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigatorBinding
    private lateinit var frameLayout: FrameLayout
    private lateinit var button: Button
    private val TAG = "NavigatorActivity"
    private var i = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        frameLayout = binding.navigatorFrameLayout
        button = binding.navigatorBtn

        button.setOnClickListener {


            val transaction = supportFragmentManager.beginTransaction()
            val f = NavigatorFragment.newInstance("i_$i")

            transaction.add(frameLayout.id, f,"NavigatorFragment_$i")
            transaction.addToBackStack("NavigatorFragment_$i")
            transaction.commit()
            i++
        }
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        //null => NavigatorFragment_1
        supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        Log.d(TAG,"back NavigatorFragment_$i")
    }
}
