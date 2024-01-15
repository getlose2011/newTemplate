package com.example.newstemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentTransaction
import com.example.newstemplate.databinding.ActivityViewPagerSimpleBinding
import com.example.newstemplate.fragment.ViewPagerSimpleFragment

class ViewPagerSimpleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPagerSimpleBinding
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        frameLayout = binding.viewPagerFragment

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(frameLayout.id, ViewPagerSimpleFragment(), ViewPagerSimpleFragment::class.java.simpleName)
        fragmentTransaction.commit()
    }
}