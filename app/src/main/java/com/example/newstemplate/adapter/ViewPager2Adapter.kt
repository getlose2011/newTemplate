package com.example.newstemplate.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newstemplate.fragment.HomeFragment


class ViewPager2Adapter
    (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            HomeFragment()
        } else if (position == 1) {
            HomeFragment()
        } else {
            HomeFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}