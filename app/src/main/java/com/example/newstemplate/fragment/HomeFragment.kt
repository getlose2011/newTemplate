package com.example.newstemplate.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.newstemplate.BaseFragment
import com.example.newstemplate.component.MyTextView
import com.example.newstemplate.databinding.FragmentHomeBinding
import com.example.newstemplate.libraries.Generic
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


interface IHomeAdapterListener{
    fun click(activity: Class<*>)
}

//如果要用 viewpager 2參考下面網址，只其它套件有滑動的可能會有相沖
//https://www.jianshu.com/p/e95f97865d10

private const val VIEWPAGER_PRE_LOAD_COUNT = 1

class HomeFragment : BaseFragment() {
    private val TAG = "HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewPager: ViewPager
    private lateinit var tableLayout: TabLayout
    private lateinit var processBar: ProgressBar
    private lateinit var tabFragmentPageAdapter: TabFragmentPageAdapter
    private lateinit var homeMenuTtf: MyTextView//menu
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewPager = binding.homeViewPager
        tableLayout = binding.homeTabs
        processBar = binding.inBaseProgressbarOverlay.baseLoadingProgressBar
        homeMenuTtf = binding.homeMenuTtf
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        getCategoryData()
        Log.d(TAG, "onViewCreated: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: ")
        viewPager.adapter = null
        super.onDestroyView()
        _binding = null
    }

    private fun initComponent() {
        tabFragmentPageAdapter = TabFragmentPageAdapter(mBaseFragmentActivity,  childFragmentManager)//requireActivity().supportFragmentManager)//

        viewPager.apply {
            offscreenPageLimit = VIEWPAGER_PRE_LOAD_COUNT
            Generic.setViewConnectAdapterOrNotify(this, tabFragmentPageAdapter)
        }

        tableLayout.apply {
            setupWithViewPager(viewPager)
        }

        homeMenuTtf.setOnClickListener {
            updateCategorySort()
        }
    }

    /**
     * 如果有更新分類排序
     * */
    private fun updateCategorySort(){

        tabFragmentPageAdapter.clearFragments()

        var data = ArrayList<HomeObj>().apply {

            add(HomeObj("最新",-1))
            add(HomeObj("熱門",-2))
            add(HomeObj("健康",2))
            add(HomeObj("生活",3))
            add(HomeObj("政治",4))
        }
        data.forEach {
            tabFragmentPageAdapter.addFragment(HomeListFragment.newInstance(it.category),it.category)
        }

        //set adapter
        Generic.setViewConnectAdapterOrNotify(viewPager,tabFragmentPageAdapter)
    }

    /**
     * 取得分類資料
     * */
    private fun getCategoryData(){
        processBar.visibility = View.VISIBLE
        //取得資料
        GlobalScope.launch{
            var data = getCategoryDataFromApi()

            data.forEach {
                tabFragmentPageAdapter.addFragment(HomeListFragment.newInstance(it.category),it.category)
            }

            withContext(Dispatchers.Main) {
                processBar.visibility = View.GONE
                updateViewPager()
            }
        }
    }

    /**
     * 更新viewpager
     * */
    private fun updateViewPager(){
        if(isAdded){
            Generic.setViewConnectAdapterOrNotify(viewPager,tabFragmentPageAdapter)
        }
    }

    /**
     * 分類資料api
     * */
    private suspend fun getCategoryDataFromApi(): ArrayList<HomeObj>   {
        //取得資料
        return  withContext(Dispatchers.IO) {
            delay(2000)
            ArrayList<HomeObj>().apply {

                add(HomeObj("最新",-1))
                add(HomeObj("熱門",-2))
                add(HomeObj("政治",4))
                add(HomeObj("生活",3))
                add(HomeObj("健康",2))
            }
        }
    }

}

/**
 *
 * TabFragmentPageAdapter adapter
 * */
class TabFragmentPageAdapter(private val context: Context, fm: androidx.fragment.app.FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragments: ArrayList<Fragment> = ArrayList()
    private var tabTitles: ArrayList<String> = ArrayList()

    fun addFragment(fragment:Fragment,title:String){
        tabTitles.add(title)
        fragments.add(fragment)
    }

    fun clearFragments(){
        tabTitles.clear()
        fragments.clear()

    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun getCount(): Int {
        return fragments.count()
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}

/**
 * 分類物件
 * */
data class HomeObj(val category:String, val id:Int)