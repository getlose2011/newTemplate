package com.example.newstemplate.fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.example.newstemplate.BaseFragment
import com.example.newstemplate.ImageSliderObj
import com.example.newstemplate.R
import com.example.newstemplate.databinding.FragmentHomeListBinding
import com.example.newstemplate.libraries.Generic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ARG_PARAM1 = "param1"
class HomeListFragment : BaseFragment() {
    //layout binding
    private val TAG = "HomeListFragment"
    private var job: Job? = null
    private var _binding: FragmentHomeListBinding? = null
    private lateinit var categoryName: String
    private val binding get() = _binding!!
    private lateinit var categoryTxt: TextView
    private lateinit var imageSlider: SliderLayout
    private lateinit var imageSlider2: SliderLayout
    private lateinit var imageSliderIndicatorLinearLayout: LinearLayout
    private lateinit var imageSliderIndicatorLinearLayout2: LinearLayout
    private lateinit var processBar: ProgressBar
    private var sliderImages: ArrayList<ImageSliderObj> = arrayListOf()

    private lateinit var newsListFlashLinearLayout: LinearLayout
    private lateinit var newsListFlashTextView: TextView
    private val flashObjList : ArrayList<FlashObj> = arrayListOf()
    //顯示跑馬燈第幾筆資料
    private var mNewsFlashDataIndex = -1
    //slider image tag
    private val sliderTag1: String = "sliderTag1"
    private val sliderTag2: String = "sliderTag2"
    //multi
    private val sliderImageObjList = arrayListOf<SliderImageObj>().apply {
        add(
            SliderImageObj(sliderTag1)
        )
        add(
            SliderImageObj(sliderTag2)
        )
    }
    //螢幕寬
    private val w by lazy {
        Generic.getScreenSize(mBaseFragmentActivity).width
    }
    //image slider 預設首圖位置
    //private var currentIndicatorPosition = 0
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
        categoryTxt = binding.homeCategoryTxt
        //progressbar
        processBar = binding.inBaseProgressbarOverlay.baseLoadingProgressBar

        //SliderLayout,SliderLayout2
        imageSlider = binding.homeListImageSlider.apply {
            layoutParams.height = w / 16 * 9
        }
        imageSlider2 = binding.homeListImageSlider2.apply {
            layoutParams.height = w / 16 * 9
        }

        //indicator,indicator2
        imageSliderIndicatorLinearLayout = binding.homeListIndicatorLinearLayout
        imageSliderIndicatorLinearLayout2 = binding.homeListIndicatorLinearLayout2

        //flash
        newsListFlashLinearLayout = binding.homeListFlashLinearLayout
        newsListFlashTextView = binding.homeListFlashTextView
        //flash設定動畫高度
        binding.homeListBreakingArrowLottie.layoutParams.apply {
            height = (newsListFlashTextView.textSize*1.5).toInt()
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        categoryTxt.text = categoryName
        if(sliderImages.any()){
            Log.d(TAG, "onViewCreated: any, $categoryName")
            setSliderImage()
        }else{
            Log.d(TAG, "onViewCreated: $categoryName")
            getData()
        }
    }
    override fun onStart() {
        sliderImageObjList.forEach {sliderImages->
            sliderImages.sliderLayoutId?.startAutoCycle(3000L, 3000L, true)
            //setCycle()

            sliderImages.sliderLayoutId?.addOnPageChangeListener(sliderImages.listener)
        }
        super.onStart()
        Log.d(TAG, "onStart: $categoryName")
    }
    override fun onResume() {

        super.onResume()
        Log.d(TAG, "onResume: $categoryName")
    }

    override fun onPause() {


        super.onPause()
        Log.d(TAG, "onPause: $categoryName")
    }


    override fun onStop() {
        sliderImageObjList.forEach {sliderImages->
            sliderImages.sliderLayoutId?.stopAutoCycle()
            sliderImages.sliderLayoutId?.removeOnPageChangeListener(sliderImages.listener)
        }
        //job?.cancel()
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
        sliderImageObjList.find {
            it.tag == sliderTag1
        }?.apply {
            sliderLayoutId = imageSlider
            indicateLayoutId = imageSliderIndicatorLinearLayout
        }
        sliderImageObjList.find {
            it.tag == sliderTag2
        }?.apply {
            sliderLayoutId = imageSlider2
            indicateLayoutId = imageSliderIndicatorLinearLayout2
        }
        sliderImageObjList.forEach {
            it.listener = listerner(it)
        }
    }
    /**
     *
     * 設定image slider
     * */
    private fun setSliderImage() {
        Log.d(TAG, "setSliderImage: ${sliderImages.count()}")
        addSliderImage()
        setIndicator()
    }
    private fun setIndicator() {

        sliderImageObjList.forEach {sliderImageObj ->
            val count = sliderImageObj.data.count()
            if(count < 2) {
                sliderImageObj.indicateLayoutId?.visibility = View.GONE
            }else{
                sliderImageObj.indicateLayoutId?.removeAllViews()

                sliderImageObj.indicateLayoutId?.visibility = View.VISIBLE
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
                    sliderImageObj.indicateLayoutId?.addView(image,layoutParams)
                }
                //設定 image slider indicator
                changeIndicator(sliderImageObj.indicateLayoutId, sliderImageObj.currentIndicatorPosition)
            }
        }
    }

    private fun listerner(sliderImageObj:SliderImageObj) = object: ViewPagerEx.OnPageChangeListener{

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            //TODO("Not yet implemented")
        }
        override fun onPageSelected(position: Int) {
            Log.d(TAG, "onPageSelected: $position, $categoryName , ${sliderImageObj.tag}")
            changeIndicator(sliderImageObj.indicateLayoutId, position, sliderImageObj.currentIndicatorPosition)
            sliderImageObj.currentIndicatorPosition = position
        }
        override fun onPageScrollStateChanged(state: Int) {
            //TODO("Not yet implemented")
        }
    }

    /**
     *
     * 改變image slider indicator 圖示
     * */
    private fun changeIndicator(imageSliderIndicatorLinearLayout:LinearLayout?, currentIndicate:Int, beforeIndicate:Int = -1){
        imageSliderIndicatorLinearLayout?.let {
            if(it.childCount > 0){
                it.getChildAt(currentIndicate).setBackgroundResource(R.drawable.selected)
                if(beforeIndicate != -1)it.getChildAt(beforeIndicate).setBackgroundResource(R.drawable.unselected)
            }
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
                    val sliderView = TextSliderView(mBaseFragmentActivity).apply {
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

                    }

                    //圖片及預設圖片
                    if (!imageSliderObj.imageUrl.isNullOrBlank())
                        sliderView.image(imageSliderObj.imageUrl).empty(R.mipmap.image_default)
                    else
                        sliderView.image(R.mipmap.image_default)

                    sliderImages.sliderLayoutId?.addSlider(sliderView)
                }

                sliderImages.sliderLayoutId?.apply {
                    //每次重載都從第一筆開始顯示
                    if (currentPosition > 0) {
                        setCurrentPosition(sliderImages.data.size - 1, true)
                        moveNextPosition()
                    } else
                        setCurrentPosition(0, true)

                    //開始的圖片位置

                    //delay => 第一次執行滑動時間
                    //duration => 每次滑動的時間
                    //autoRecover => false 使用者手指停留在該圖片時，則會停留在該圖片，不會繼續在滑動

                    sliderImages.sliderLayoutId?.startAutoCycle(3000L, 3000L, true)
                }
            }
        }
    }


    private fun setFlash() {
        newsListFlashLinearLayout.visibility = View.VISIBLE

        val setCurrentNewsFlashFunc = {
            mNewsFlashDataIndex = if (mNewsFlashDataIndex == flashObjList.lastIndex) 0 else mNewsFlashDataIndex + 1
            val model = flashObjList[mNewsFlashDataIndex]

            newsListFlashTextView.apply {
                text = model.title
                setOnClickListener {
                    Toast.makeText(mBaseFragmentActivity, model.title, Toast.LENGTH_LONG).show()
                }
            }
        }

        setCurrentNewsFlashFunc()

        //有兩筆(含)以上資料才使用動畫效果
        if(flashObjList.size > 1){
            //animation
            //中間 > 上面
            val mNewsFlashCenterToTopAnim = AnimationUtils.loadAnimation(mBaseFragmentActivity,
                R.anim.news_flash_center_to_top_animation)

            //下面 > 中間
            val mNewsFlashBottomToCenterAnim = AnimationUtils.loadAnimation(mBaseFragmentActivity,
                R.anim.news_flash_bottom_to_center_animation)

            mNewsFlashCenterToTopAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    newsListFlashTextView.apply {
                        startAnimation(mNewsFlashBottomToCenterAnim)
                        setCurrentNewsFlashFunc()
                    }
                }

                override fun onAnimationStart(animation: Animation?) {}
            })

            mNewsFlashBottomToCenterAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    newsListFlashTextView.apply {
                        startAnimation(mNewsFlashCenterToTopAnim)
                    }
                }

                override fun onAnimationStart(animation: Animation?) {}
            })

            //啟動動畫
            newsListFlashTextView.apply {
                startAnimation(mNewsFlashCenterToTopAnim)
            }
        }
    }

    private fun getData() {
        processBar.visibility = View.VISIBLE
        //取得資料
        job = GlobalScope.launch(Dispatchers.Main) {
            //main 1
            processBar.visibility = View.VISIBLE
            withContext(Dispatchers.IO){
                // not ui thread 2
                var data = getDataFromApi()
                if(data.success){
                    //slider image
                    sliderImageObjList.find {
                        it.tag == sliderTag1
                    }?.data?.apply{
                        clear()
                        addAll(data.sliderImages)
                    }
                    sliderImageObjList.find {
                        it.tag == sliderTag2
                    }?.data?.apply{
                        clear()
                        addAll(data.sliderImages1)
                    }
                    //flash
                    flashObjList.apply {
                        clear()
                        addAll(data.flashObjData)
                    }
                }
            }
            //main 3
            processBar.visibility = View.GONE
            //slider image
            setSliderImage()
            //flash
            setFlash()
        }
    }


    private suspend fun getDataFromApi(): HomeListModel {
        return withContext(Dispatchers.IO) {
            delay(4000)
            HomeListModel(true).apply {
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
                flashObjData.apply {
                    add(FlashObj(1,"下垂暗沉蠟黃皺紋找上門？不是年齡問題而是「糖化臉」",""))
                    add(FlashObj(2,"快訊每天喝咖啡有益健康？營養師示警3類人要小心",""))
                    add(FlashObj(3,"沒船沒飛機！金門人返鄉投票卡關 他1方案比投訴還快","https://news.ebc.net.tw/news/living/399860"))
                }
            }
        }
    }
}
data class HomeListModel(
    val success: Boolean,
    val message: String="",
    val sliderImages: ArrayList<ImageSliderObj> = arrayListOf(),
    val sliderImages1: ArrayList<ImageSliderObj> = arrayListOf(),
    val flashObjData : ArrayList<FlashObj> = arrayListOf()
)
data class SliderImageObj(
    val tag:String,
    var sliderLayoutId:SliderLayout? = null,
    var indicateLayoutId:LinearLayout? = null,
    var currentIndicatorPosition: Int=0,
    val data: ArrayList<ImageSliderObj> = arrayListOf(),
    var listener: ViewPagerEx.OnPageChangeListener? = null
)

data class FlashObj(val id:Int, val title:String, val url:String)