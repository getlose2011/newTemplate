package com.example.newstemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.newstemplate.databinding.ActivityFlashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FlashActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityFlashBinding
    private lateinit var newsListFlashLinearLayout: LinearLayout
    private lateinit var newsListFlashTextView: TextView
    private val flashObjList : ArrayList<FlashObj> = arrayListOf()
    //顯示跑馬燈第幾筆資料
    private var mNewsFlashDataIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashBinding.inflate(layoutInflater)
        newsListFlashLinearLayout = binding.newsListFlashLinearLayout
        newsListFlashTextView = binding.newsListFlashTextView
        setContentView(binding.root)

        initView()

        getData()
    }

    override fun onStop() {
        mNewsFlashDataIndex = -1
        newsListFlashTextView.clearAnimation()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initView() {

        //設定動畫高度
        binding.breakingArrowLottie.layoutParams.apply {
            height = (binding.newsListFlashTextView.textSize*1.5).toInt()
        }

    }

    /***
     *
     * 跑馬燈文字動畫
     * */
    private fun setNewsFlash() {

        newsListFlashLinearLayout.visibility = View.VISIBLE

        val setCurrentNewsFlashFunc = {
            mNewsFlashDataIndex = if (mNewsFlashDataIndex == flashObjList.lastIndex) 0 else mNewsFlashDataIndex + 1
            val model = flashObjList[mNewsFlashDataIndex]

            newsListFlashTextView.apply {
                text = model.title
                setOnClickListener {
                    Toast.makeText(this@FlashActivity, model.title, Toast.LENGTH_LONG).show()
                }
            }
        }

        setCurrentNewsFlashFunc()

        //有兩筆(含)以上資料才使用動畫效果
        if(flashObjList.size > 1){
            //animation
            //中間 > 上面
            val mNewsFlashCenterToTopAnim = AnimationUtils.loadAnimation(this,
                R.anim.news_flash_center_to_top_animation)

            //下面 > 中間
            val mNewsFlashBottomToCenterAnim = AnimationUtils.loadAnimation(this,
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
     * 取得資料
     * */
    private fun getData() {

            //取得資料
        GlobalScope.launch(Dispatchers.Main) {
            //main step 1

            withContext(Dispatchers.IO) {
                // not ui thread step 2
                var data = getDataFromApi()
                flashObjList.apply {
                    clear()
                    addAll(data)
                }
            }

            //main step 3
            if(flashObjList.any())setNewsFlash()
        }

    }


    private suspend fun getDataFromApi(): ArrayList<FlashObj>   {
        //取得資料
        return  withContext(Dispatchers.IO) {
            delay(700)
            ArrayList<FlashObj>().apply {

                add(FlashObj(1,"下垂暗沉蠟黃皺紋找上門？不是年齡問題而是「糖化臉」",""))
                add(FlashObj(2,"快訊每天喝咖啡有益健康？營養師示警3類人要小心",""))
                add(FlashObj(3,"沒船沒飛機！金門人返鄉投票卡關 他1方案比投訴還快","https://news.ebc.net.tw/news/living/399860"))

            }
        }
    }

}

data class FlashObj(val id:Int, val title:String, val url:String)