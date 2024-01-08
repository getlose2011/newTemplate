package com.example.newstemplate


import android.R
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.newstemplate.databinding.ActivityHomeBinding
import com.example.newstemplate.fragment.HomeFragment
import com.example.newstemplate.fragment.HomeListFragment


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        frameLayout = binding.homeFragment

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(frameLayout.id, HomeFragment(), HomeFragment::class.java.simpleName)
        fragmentTransaction.commit()

    }

}