package com.example.newstemplate

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.newstemplate.databinding.ActivityHomeBinding
import com.example.newstemplate.fragment.HomeFragment
import com.google.android.material.tabs.TabLayout


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewPager: ViewPager
    private lateinit var tableLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        viewPager = binding.homeViewPager
        tableLayout = binding.homeTabs
        setContentView(binding.root)

        val tabFragmentPageAdapter = TabFragmentPageAdapter(this, supportFragmentManager)
        viewPager.adapter = tabFragmentPageAdapter
        tableLayout.setupWithViewPager(viewPager)

    }

}

private val TAB_TITLES = arrayOf(
    "R.string.tab_text_11",
    "R.string.tab_text_2"
)

class TabFragmentPageAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragments: List<Fragment> = listOf(HomeFragment.newInstance("1"), HomeFragment.newInstance("2"))

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        return fragments.count()
    }


}