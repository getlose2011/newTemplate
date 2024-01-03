package com.example.newstemplate.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx

import com.example.newstemplate.databinding.FragmentHomeBinding
import com.example.newstemplate.databinding.FragmentHomeListBinding
import com.example.newstemplate.databinding.NewsLayoutBinding
import com.example.newstemplate.databinding.RowMainItemBinding
import com.example.newstemplate.libraries.Generic
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ARG_PARAM1 = "param1"


interface IHomeAdapterListener{
    fun click(activity: Class<*>)
}

//https://www.jianshu.com/p/e95f97865d10
class HomeFragment : Fragment() {
    //layout binding
    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var tableLayout: TabLayout
    private var mLayoutMediator: TabLayoutMediator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewPager = _binding?.homeViewPager!!
        tableLayout = _binding?.homeTabs!!


        val fragmentStringList = arrayListOf(
            "FirstFragment",
            "SecondFragment",
            "ThirdFragment",
            "FirstFragment",
            "SecondFragment",
            "ThirdFragment",
            "FirstFragment",
            "SecondFragment",
            "ThirdFragment"
        )

        // 使用requireActivity().supportFragmentManager时 旋转后会报错
        // ViewPager2使用的问题 FragmentManager is already executing transactions
        // http://bbs.xiangxueketang.cn/question/1977
        /*
        * 尽量不要用getActivity().getSupportFragmentManager()的方式，
        * 而是getChildFragmentManager()管理
        * */
// https://issuetracker.google.com/issues/154751401
// 解决 使用 navigation + viewPager2 + recyclerview 界面切换时内存泄漏问题 注意点：
// 1.使用viewLifecycleOwner.lifecycle 而不是 lifecycle
// 2. recyclerview的adapter 在onDestroyView 中置 null
        val adapter = ViewPagerAdapter(
            fragmentStringList,
            /*requireActivity().supportFragmentManager*/
            childFragmentManager,
            //lifecycle
            viewLifecycleOwner.lifecycle
        )

        viewPager.adapter = adapter
        // 设置 offscreenPageLimit
        //viewPager.offscreenPageLimit = fragmentStringList.size -1



        //绑定 tabLayout 和 viewPager
        mLayoutMediator =  TabLayoutMediator(
            tableLayout,
            viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "First"
                1 -> tab.text = "Second"
                else -> tab.text = "Third"
            }
        }
        mLayoutMediator?.attach()

        return _binding?.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDestroyView() {

        super.onDestroyView()
        // https://stackoverflow.com/questions/61779776/leak-canary-detects-memory-leaks-for-tablayout-with-viewpager2
        // TabLayout 解绑
        mLayoutMediator?.detach()
        mLayoutMediator = null
        viewPager.adapter = null
        _binding = null
    }

}



class ViewPagerAdapter(
    private val fragmentStringList: MutableList<String>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return fragmentStringList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (fragmentStringList[position]) {
            "FirstFragment" -> HomeListFragment()
            "SecondFragment" -> HomeListFragment()
            else -> HomeListFragment()
        }
    }
}