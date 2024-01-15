package com.example.newstemplate.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.newstemplate.BaseFragment
import com.example.newstemplate.component.MyTextView
import com.example.newstemplate.databinding.FragmentViewPagerSimpleBinding
import com.example.newstemplate.libraries.Generic
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val VIEWPAGER_PRE_LOAD_COUNT = 1
/**
 * A simple [Fragment] subclass.
 * Use the [ViewPagerSimpleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

//如果要用 viewpager 2參考下面網址，只其它套件有滑動的可能會有相沖
//https://www.jianshu.com/p/e95f97865d10

class ViewPagerSimpleFragment : BaseFragment() {
    private val TAG = "ViewPagerSimpleFragment"
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var _binding: FragmentViewPagerSimpleBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager
    private lateinit var tableLayout: TabLayout
    private lateinit var processBar: ProgressBar
    private lateinit var tabFragmentPageAdapter: VpTabFragmentPageAdapter
    private lateinit var homeMenuTtf: MyTextView//menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewPagerSimpleBinding.inflate(inflater, container, false)
        viewPager = binding.vPViewPager
        tableLayout = binding.viewPagerTabs
        processBar = binding.inBaseProgressbarOverlay.baseLoadingProgressBar
        homeMenuTtf = binding.viewPagerMenuTtf
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        getCategoryData()
        Log.d(TAG, "onViewCreated: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initComponent() {
        tabFragmentPageAdapter = VpTabFragmentPageAdapter(mBaseFragmentActivity,  childFragmentManager)//requireActivity().supportFragmentManager)//

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
            tabFragmentPageAdapter.addFragment(ViewPagerSimpleListFragment.newInstance(it.category),it.category)
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
                tabFragmentPageAdapter.addFragment(ViewPagerSimpleListFragment.newInstance(it.category),it.category)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         *
         * @return A new instance of fragment ViewPagerSimpleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            ViewPagerSimpleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}

/**
 *
 * TabFragmentPageAdapter adapter
 * */
class VpTabFragmentPageAdapter(private val context: Context, fm: androidx.fragment.app.FragmentManager) :
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