package com.example.newstemplate.fragment



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
import androidx.appcompat.widget.LinearLayoutCompat
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.example.newstemplate.BaseFragment
import com.example.newstemplate.Model.CoverTopicModel
import com.example.newstemplate.Model.FlashNewsModel
import com.example.newstemplate.Model.NewsSliderImageModel
import com.example.newstemplate.Model.ResponseModel
import com.example.newstemplate.R
import com.example.newstemplate.databinding.FragmentHomeListBinding
import com.example.newstemplate.libraries.Generic
import com.example.newstemplate.service.HomeListService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"

class HomeListFragment : BaseFragment() {
    //layout binding
    private val TAG = "HomeListFragment"
    private var job: Job? = null
    private var _binding: FragmentHomeListBinding? = null
    private lateinit var categoryName: String
    private val binding get() = _binding!!
    private lateinit var categoryTxt: TextView
    private lateinit var newsImageSliderLayout: SliderLayout
    private lateinit var coverTopicSliderLayout: SliderLayout
    private lateinit var newsImageIndicatorLinearLayout: LinearLayout
    private lateinit var coverTopicIndicatorLinearLayout: LinearLayout
    private lateinit var newsImageListener: ViewPagerEx.OnPageChangeListener
    private lateinit var coverTopicListener: ViewPagerEx.OnPageChangeListener
    private lateinit var processBar: ProgressBar
    private lateinit var newsListFlashLinearLayout: LinearLayout
    private lateinit var newsListFlashTextView: TextView
    //news image slider data
    private var newsSliderImageList = arrayListOf<NewsSliderImageModel>()
    private var mSliderImageIndex = 0
    //cover topic
    private var coverTopicList : ArrayList<CoverTopicModel> = arrayListOf()
    private var mCoverTopicIndex = 0
    //flash news data
    private var flashNewsList : ArrayList<FlashNewsModel> = arrayListOf()
    //顯示跑馬燈第幾筆資料
    private var mNewsFlashDataIndex = -1


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryName = "${it.getString(ARG_PARAM1, "")}"
        }
        Log.d(TAG, "onCreated: any, $categoryName")
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
        newsImageSliderLayout = binding.homeListImageSlider.apply {
            layoutParams.height = w / 16 * 9
        }
        coverTopicSliderLayout = binding.homeListImageSlider2.apply {
            layoutParams.height = w / 16 * 9
        }

        //indicator,indicator2
        newsImageIndicatorLinearLayout = binding.homeListIndicatorLinearLayout
        coverTopicIndicatorLinearLayout = binding.homeListIndicatorLinearLayout2

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
        getData()
    }
    override fun onStart() {
        super.onStart()
        //start
        newsImageSliderLayout.startAutoCycle(3000L, 3000L, true)
        coverTopicSliderLayout.startAutoCycle(3000L, 3000L, true)
        //listener
        newsImageSliderLayout.addOnPageChangeListener(newsImageListener)
        coverTopicSliderLayout.addOnPageChangeListener(coverTopicListener)
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
        //stop
        newsImageSliderLayout.stopAutoCycle()
        coverTopicSliderLayout.stopAutoCycle()
        //listener
        newsImageSliderLayout.removeOnPageChangeListener(newsImageListener)
        coverTopicSliderLayout.removeOnPageChangeListener(coverTopicListener)
        //job?.cancel()
        super.onStop()
        Log.d(TAG, "onStop: $categoryName")
    }
    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: $categoryName")

        super.onDestroyView()

        _binding = null
    }
    override fun onDestroy() {
        Log.d(TAG, "onDestroy: $categoryName")
        super.onDestroy()
    }
    private fun initComponent() {
        //設定listner
        newsImageListener = setSliderLayoutListener(SliderImageEnum.News)
        coverTopicListener = setSliderLayoutListener(SliderImageEnum.CoverTopic)
    }
    /**
     *
     * 設定輪播新聞
     * */
    private fun setNewsSliderImage() {
        if(newsSliderImageList.any()){
            mSliderImageIndex = 1

            newsImageIndicatorLinearLayout.visibility = View.VISIBLE

            setSliderLayout(newsImageSliderLayout){

                newsSliderImageList.forEach {
                    val textSliderView = setTextSliderView(it.title,it.imageUrl,ClickState.ShowText(it.title))
                    newsImageSliderLayout.addSlider(textSliderView)
                }

                setSliderLayoutCurrentPosition(newsImageSliderLayout,newsSliderImageList.size-(newsSliderImageList.size-mSliderImageIndex) - 1)

            }

            //indicator
            setIndicator(newsImageIndicatorLinearLayout,newsSliderImageList.count(),mSliderImageIndex)
        }
    }

    /**
     *
     * 設定精選主題
     * */
    private fun setCoverTopicImage() {
        if(coverTopicList.any()){

            mCoverTopicIndex = 1

            coverTopicSliderLayout.visibility = View.VISIBLE
            coverTopicIndicatorLinearLayout.visibility = View.VISIBLE

            setSliderLayout(coverTopicSliderLayout){

                coverTopicList.forEach {
                    val textSliderView = setTextSliderView(it.title,it.imageUrl,ClickState.ShowText(it.title))
                    coverTopicSliderLayout.addSlider(textSliderView)
                }

                setSliderLayoutCurrentPosition(coverTopicSliderLayout,newsSliderImageList.size-(newsSliderImageList.size-mCoverTopicIndex)-1)

            }

            //indicator
            setIndicator(coverTopicIndicatorLinearLayout, coverTopicList.count(),mCoverTopicIndex)

        }
    }

    private fun setSliderLayoutListener(si: SliderImageEnum) = object: ViewPagerEx.OnPageChangeListener{

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            //TODO("Not yet implemented")
        }
        override fun onPageSelected(position: Int) {

            if(si == SliderImageEnum.News){
                Log.d(TAG, "onPageSelected: $categoryName $si $position $mSliderImageIndex s")
                setActiveIndicator(newsImageIndicatorLinearLayout, position, mSliderImageIndex)
                mSliderImageIndex = position
            }else if(si == SliderImageEnum.CoverTopic){
                Log.d(TAG, "onPageSelected: $categoryName $si $position $mCoverTopicIndex c")
                setActiveIndicator(coverTopicIndicatorLinearLayout, position, mCoverTopicIndex)
                mCoverTopicIndex = position
            }
        }
        override fun onPageScrollStateChanged(state: Int) {
            //TODO("Not yet implemented")
        }
    }

    private fun setSliderLayout(sliderLayout: SliderLayout,callback:()->Unit) {
        //先停止
        sliderLayout.stopAutoCycle()
        sliderLayout.removeAllSliders()
        callback()
    }


    private fun setTextSliderView(title: String, imageUrl: String, clickSt: ClickState): TextSliderView {
        val sliderView = TextSliderView(mBaseFragmentActivity).apply {
            //文字
            description(title)
            //圖片的比例類型。
            scaleType = BaseSliderView.ScaleType.Fit
            //點選圖片事件
            setOnSliderClickListener {
                clickOp(clickSt)
            }

        }

        //圖片及預設圖片
        if (imageUrl.isNotBlank())
            sliderView.image(imageUrl).empty(R.mipmap.image_default)
        else
            sliderView.image(R.mipmap.image_default)

        return sliderView
    }

    private fun setSliderLayoutCurrentPosition(sliderLayout: SliderLayout, position:Int) {


        sliderLayout.apply {
            //currentPosition = 1
            //每次重載都從第一筆開始顯示
            Log.d(TAG, "addSliderImage: $currentPosition $position $categoryName")

            if (currentPosition > 0) {
                setCurrentPosition(position , true)
                moveNextPosition()
            } else
                setCurrentPosition(0, true)

            //開始的圖片位置

            //delay => 第一次執行滑動時間
            //duration => 每次滑動的時間
            //autoRecover => false 使用者手指停留在該圖片時，則會停留在該圖片，不會繼續在滑動

            startAutoCycle(3000L, 3000L, true)
        }
    }

    /**
     *
     * 設定image slider
     * */
    private fun setIndicator(indicatorLayout: LinearLayout, count:Int, currentIndicatorPosition:Int) {

            if(count < 2) {
                indicatorLayout.visibility = View.GONE
            }else{
                indicatorLayout.removeAllViews()

                //indicatorLayout.visibility = View.VISIBLE
                val indicatorWidth = (w - 350)/count
                (0 until count).forEach { i ->
                    var image = ImageView(mBaseFragmentActivity)
                    image.setBackgroundResource(R.drawable.unselected)
                    //如果要加下面那行，比重一樣所以寬有設跟沒設一樣，imageSliderIndicatorLinearLayout 會滿版
                    //layoutParams.weight = 1f
                    // Set layout parameters
                    val layoutParams = LinearLayoutCompat.LayoutParams(
                        indicatorWidth,
                        8
                    )
                    //第一個左邊不設left margin
                    if(i != 0)layoutParams.leftMargin = 10
                    //a.方法
                    //image.layoutParams = layoutParams
                    //imageSliderIndicatorLinearLayout.addView(image)
                    //b.方法
                    indicatorLayout.addView(image,layoutParams)
                }
                //設定 image slider indicator
                setActiveIndicator(indicatorLayout, currentIndicatorPosition)
            }

    }

    /**
     *
     * 改變image slider indicator 圖示
     * */
    private fun setActiveIndicator(indicatorLayout:LinearLayout, currentIndicate:Int, beforeIndicate:Int = -1){
        indicatorLayout.let {
            if(it.childCount > 0){
                it.getChildAt(currentIndicate).setBackgroundResource(R.drawable.selected)
                if(beforeIndicate != -1)it.getChildAt(beforeIndicate).setBackgroundResource(R.drawable.unselected)
            }
        }
    }


    /**
     * 設定快訊
     * */
    private fun setNewsFlash() {
        newsListFlashLinearLayout.visibility = View.VISIBLE

        val setCurrentNewsFlashFunc = {
            mNewsFlashDataIndex = if (mNewsFlashDataIndex == flashNewsList.lastIndex) 0 else mNewsFlashDataIndex + 1
            val model = flashNewsList[mNewsFlashDataIndex]

            newsListFlashTextView.apply {
                text = model.title
                setOnClickListener {
                    //Toast.makeText(mBaseFragmentActivity, model.title, Toast.LENGTH_LONG).show()
                    clickOp(ClickState.OpenWeb("https://news.ebc.net.tw"))
                }
            }
        }

        setCurrentNewsFlashFunc()

        //有兩筆(含)以上資料才使用動畫效果
        if(flashNewsList.size > 1){
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


    /**
     * 取得全部資料
     * */
    private fun getData(){
        job = GlobalScope.launch(Dispatchers.Main) {

            // main
            Log.d(TAG, "getdata4_1")
            processBar.visibility = View.VISIBLE

            val newsSliderImageResponseModel: Deferred<ResponseModel<ArrayList<NewsSliderImageModel>>> = GlobalScope.async(Dispatchers.IO) {
                //not ui
                HomeListService().getNewsSliderImageApi()
            }

            val flashNewsResponseModel: Deferred<ResponseModel<ArrayList<FlashNewsModel>>> = GlobalScope.async(Dispatchers.IO) {
                //not ui
                HomeListService().getFlashNewsApi()
            }

            val coverTopicResponseModel: Deferred<ResponseModel<ArrayList<CoverTopicModel>>> = GlobalScope.async(Dispatchers.IO) {
                //not ui
                HomeListService().getCoverTopicApi()
            }

            //main
            val newsSliderImageResult = newsSliderImageResponseModel.await()
            val flashNewsResult = flashNewsResponseModel.await()
            val coverTopicResult = coverTopicResponseModel.await()

            //輪播新聞
            if(newsSliderImageResult.success){
                newsSliderImageList.apply {
                    clear()
                    newsSliderImageResult.data?.let { addAll(it) }
                }
                setNewsSliderImage()
            }else{

            }

            //快訊新聞
            if(flashNewsResult.success){
                flashNewsList.apply {
                    clear()
                    flashNewsResult.data?.let { addAll(it) }
                }
                setNewsFlash()
            }else{

            }

            //精選主題
            if(coverTopicResult.success){
                coverTopicList.apply {
                    clear()
                    coverTopicResult.data?.let { addAll(it) }
                }
                setCoverTopicImage()
            }else{

            }
            processBar.visibility = View.GONE
        }
    }



    /**
     *
     * 操作按了click事件
     * **/
    private fun clickOp(click:ClickState){
        when(click){
            is ClickState.OpenWeb->{
                Toast.makeText(mBaseFragmentActivity,click.url,Toast.LENGTH_LONG).show()
            }
            is ClickState.ShowText->{
                Toast.makeText(mBaseFragmentActivity,click.txt,Toast.LENGTH_LONG).show()
            }
        }
    }

    enum class SliderImageEnum {
        News,CoverTopic
    }
}



sealed class ClickState {
    // data class
    data class ShowText(val txt: String): ClickState()
    data class OpenWeb(val url: String): ClickState()

}