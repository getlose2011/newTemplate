package com.example.newstemplate.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.example.newstemplate.ImageSliderObj
import com.example.newstemplate.R
import com.example.newstemplate.databinding.FragmentHomeListBinding
import com.example.newstemplate.libraries.Generic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ARG_PARAM1 = "param1"

class HomeListFragment : Fragment() {
    //layout binding
    private val TAG = "HomeListFragment"
    private var _binding: FragmentHomeListBinding? = null
    private lateinit var categoryName: String
    private val binding get() = _binding!!
    private lateinit var imageSlider: SliderLayout
    private lateinit var imageSlider2: SliderLayout
    private lateinit var imageSliderIndicatorLinearLayout: LinearLayout
    private lateinit var imageSliderIndicatorLinearLayout2: LinearLayout
    private lateinit var processBar: ProgressBar
    private var sliderImages: ArrayList<ImageSliderObj> = arrayListOf()
    private lateinit var mBaseFragmentActivity: AppCompatActivity
    private var isResume: Boolean = false
    //multi
    private val sliderImageObjList = arrayListOf<SliderImageObj>().apply {
        add(
            SliderImageObj("1")
        )
        add(
            SliderImageObj("2")
        )
    }
    //螢幕寬
    private val w by lazy {
        Generic.widthPx(mBaseFragmentActivity)
    }
    //image slider 預設首圖位置
    private var currentIndicatorPosition = 0

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            HomeListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.also {
            mBaseFragmentActivity = it as AppCompatActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            categoryName = "${it.getString(ARG_PARAM1, "")}"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeListBinding.inflate(inflater, container, false)
        //SliderLayout高度
        imageSlider = binding.homeListImageSlider.apply {
            layoutParams.height = w / 16 * 9
        }
        //SliderLayout高度
        imageSlider2 = binding.homeListImageSlider2.apply {
            layoutParams.height = w / 16 * 9
        }
        imageSliderIndicatorLinearLayout = binding.homeListIndicatorLinearLayout
        imageSliderIndicatorLinearLayout2 = binding.homeListIndicatorLinearLayout2
        processBar = binding.inBaseProgressbarOverlay.baseLoadingProgressBar

        sliderImageObjList.find {
            it.tag == "1"
        }?.apply {
            sliderLayoutId = imageSlider
            indicateLayoutId = imageSliderIndicatorLinearLayout
        }

        sliderImageObjList.find {
            it.tag == "2"
        }?.apply {
            sliderLayoutId = imageSlider2
            indicateLayoutId = imageSliderIndicatorLinearLayout2
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()

        if(sliderImages.any()){
            Log.d(TAG, "onViewCreated: any, $categoryName")
            setSliderImage()
        }else{
            Log.d(TAG, "onViewCreated: $categoryName")
            getData()
        }


    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: $categoryName")
    }

    override fun onResume() {
        isResume = true

        sliderImageObjList.forEach {sliderImages->
            sliderImages.sliderLayoutId?.startAutoCycle(3000L, 3000L, true)
            //setCycle()
            sliderImages.sliderLayoutId?.addOnPageChangeListener(sliderListener)
        }


        super.onResume()
        Log.d(TAG, "onResume: $categoryName")
    }

    override fun onPause() {
        isResume = false
        sliderImageObjList.forEach {sliderImages->
            sliderImages.sliderLayoutId?.stopAutoCycle()
            sliderImages.sliderLayoutId?.removeOnPageChangeListener(sliderListener)
        }

        super.onPause()
        Log.d(TAG, "onPause: $categoryName")
    }

    override fun onStop() {

        super.onStop()
        Log.d(TAG, "onStop: $categoryName")
    }

    override fun onDestroyView() {

        Log.d(TAG, "onDestroyView: $categoryName")
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: $categoryName")
        super.onDestroy()
    }


    private fun initComponent() {
        //imageSlider.addOnPageChangeListener(sliderListener)
    }

    /**
     *
     * 設定image slider
     * */
    private fun setSliderImage() {

        Log.d(TAG, "setSliderImage: ${sliderImages.count()}")
        addSliderImage()
        //setIndicator()
    }

    private fun setIndicator() {
        imageSliderIndicatorLinearLayout.removeAllViews()
        val count = sliderImages.count()
        if(count < 2) {
            imageSliderIndicatorLinearLayout.visibility = View.GONE
            return
        }

        imageSliderIndicatorLinearLayout.visibility = View.VISIBLE

        val indicateWidth = (w - 350)/count

        (0 until count).forEach { i ->
            var image = ImageView(mBaseFragmentActivity)
            image.setBackgroundResource(R.drawable.unselected)
            //如果要加下面那行，比重一樣所以寬有設跟沒設一樣，imageSliderIndicatorLinearLayout 會滿版
            //layoutParams.weight = 1f
            // Set layout parameters
            val layoutParams = LinearLayoutCompat.LayoutParams(
                indicateWidth,
                8
            )

            //第一個左邊不設left margin
            if(i != 0)layoutParams.leftMargin = 10

            //a.方法
            //image.layoutParams = layoutParams
            //imageSliderIndicatorLinearLayout.addView(image)
            //b.方法
            imageSliderIndicatorLinearLayout.addView(image,layoutParams)

        }

        //設定 image slider indicator
        changeIndicator(currentIndicatorPosition)

    }

    val sliderListener = object: ViewPagerEx.OnPageChangeListener{
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            //TODO("Not yet implemented")
        }

        override fun onPageSelected(position: Int) {
            Log.d(TAG, "onPageSelected: $position, $categoryName")
            changeIndicator(position,currentIndicatorPosition)
            currentIndicatorPosition = position
        }

        override fun onPageScrollStateChanged(state: Int) {
            //TODO("Not yet implemented")
        }

    }

    /**
     *
     * 改變image slider indicator 圖示
     * */
    private fun changeIndicator(currentIndicate:Int,beforeIndicate:Int = -1){
        if(imageSliderIndicatorLinearLayout.childCount > 0){
            imageSliderIndicatorLinearLayout.getChildAt(currentIndicate).setBackgroundResource(R.drawable.selected)
            if(beforeIndicate != -1)imageSliderIndicatorLinearLayout.getChildAt(beforeIndicate).setBackgroundResource(R.drawable.unselected)

        }
    }

    /**
     * 圖片加載到SliderLayout
     *
     * */
    private fun addSliderImage() {
        //圖片加載到imageSlider

        sliderImageObjList.forEach {sliderImages->

            if(sliderImages.data.any()){
                //先停止
                sliderImages.sliderLayoutId?.stopAutoCycle()
                sliderImages.sliderLayoutId?.removeAllSliders()
                sliderImages.data.forEach { imageSliderObj ->
                    //slider object
                    TextSliderView(mBaseFragmentActivity).apply {
                        //文字
                        description(imageSliderObj.title)
                        //圖片的比例類型。
                        scaleType = BaseSliderView.ScaleType.Fit
                        //點選圖片事件

                        setOnSliderClickListener {
                            Toast.makeText(
                                mBaseFragmentActivity, imageSliderObj.title,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        //圖片及預設圖片
                        image(imageSliderObj.imageUrl).empty(R.mipmap.image_default)
                    }.also {
                        //加入到 SliderLayout
                        sliderImages.sliderLayoutId?.addSlider(it)
                    }
                    sliderImages.sliderLayoutId?.apply {
                        //開始的圖片位置
                        setCurrentPosition(0, true)
                        //delay => 第一次執行滑動時間
                        //duration => 每次滑動的時間
                        //autoRecover => false 使用者手指停留在該圖片時，則會停留在該圖片，不會繼續在滑動
                        if(isResume)sliderImages.sliderLayoutId?.startAutoCycle(3000L, 3000L, true)
                    }
                }

            }
        }

    }



    private fun getData() {
        processBar.visibility = View.VISIBLE
        //取得資料
        GlobalScope.launch {

            var data = getDataFromApi()

            withContext(Dispatchers.Main) {
                processBar.visibility = View.GONE

                if(data.success){
                    //slider image
                    sliderImageObjList.find {
                        it.tag == "1"
                    }?.data?.apply{
                        clear()
                        addAll(data.sliderImages)
                    }

                    sliderImageObjList.find {
                        it.tag == "2"
                    }?.data?.apply{
                        clear()
                        addAll(data.sliderImages1)
                    }

                    setSliderImage()
                }
            }
        }
    }

    private suspend fun getDataFromApi(): HomeListObj {
        return withContext(Dispatchers.IO) {
            delay(1500)
            HomeListObj(true).apply {
                sliderImages1.apply {
                    add(
                        ImageSliderObj(
                            "https://img.news.ebc.net.tw/EbcNews/news/2024/01/05/1704472623_55845.jpg",
                            "4疑收中方資金參選立委！ 前民眾黨桃園黨部發言人馬治薇遭聲押"
                        )
                    )
                    add(
                        ImageSliderObj(
                            "https://img.news.ebc.net.tw/EbcNews/news/2024/01/05/1704474375_79604.jpg",
                            "5張誌家猝逝「2前妻靈前陪伴」 哥曝改名內幕"
                        )
                    )
                    add(
                        ImageSliderObj(
                            "https://img.news.ebc.net.tw/EbcNews/news/2024/01/05/1704474798_62173.jpg",
                            "6未接種XBB！羅一鈞：保護力恐歸零 1族群更要注意"
                        )
                    )
                }
                sliderImages.apply {

                    add(
                        ImageSliderObj(
                            "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698748147_66456.jpg",
                            "1高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個 成長突飛猛進1"
                        )
                    )
                    add(
                        ImageSliderObj(
                            "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747935_57304.jpg",
                            "2高山峰親曝給兒子吃這個 成長突飛猛進2"
                        )
                    )
                    add(
                        ImageSliderObj(
                            "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747945_53957.jpg",
                            "3高山峰親曝給兒子吃這個 成長突飛猛進3"
                        )
                    )
                    add(
                        ImageSliderObj(
                            "https://img.news.ebc.net.tw/EbcNews/news/2024/01/05/1704472623_55845.jpg",
                            "4疑收中方資金參選立委！ 前民眾黨桃園黨部發言人馬治薇遭聲押"
                        )
                    )
                    add(
                        ImageSliderObj(
                            "https://img.news.ebc.net.tw/EbcNews/news/2024/01/05/1704474375_79604.jpg",
                            "5張誌家猝逝「2前妻靈前陪伴」 哥曝改名內幕"
                        )
                    )
                    add(
                        ImageSliderObj(
                            "https://img.news.ebc.net.tw/EbcNews/news/2024/01/05/1704474798_62173.jpg",
                            "6未接種XBB！羅一鈞：保護力恐歸零 1族群更要注意"
                        )
                    )
                }
            }
        }
    }
}

data class HomeListObj(
    val success: Boolean,
    val message: String="",
    val sliderImages: ArrayList<ImageSliderObj> = arrayListOf(),
    val sliderImages1: ArrayList<ImageSliderObj> = arrayListOf()
)

data class SliderImageObj(
    val tag:String,
    var sliderLayoutId:SliderLayout? = null,
    var indicateLayoutId:LinearLayout? = null,
    val currentIndicatorPosition: Int=0,
    val data: ArrayList<ImageSliderObj> = arrayListOf()
)