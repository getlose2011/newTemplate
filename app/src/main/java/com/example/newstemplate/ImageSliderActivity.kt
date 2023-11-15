package com.example.newstemplate

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.example.newstemplate.databinding.ActivityImageSliderBinding
import com.example.newstemplate.libraries.Generic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ImageSliderObj(val imageUrl:String,val title:String)

class ImageSliderActivity : AppCompatActivity() {

    private val TAG = "ImageSliderActivity"
    private lateinit var binding: ActivityImageSliderBinding
    private lateinit var imageSlider: SliderLayout
    private lateinit var imageSliderIndicatorLinearLayout: LinearLayoutCompat
    //螢幕寬
    private val w by lazy {
        Generic.widthPx(this@ImageSliderActivity)
    }
    //image slider 預設首圖位置
    private var currentIndicatorPosition = 0
    //image slider 少於幾張圖,要處理的動作
    private var imageSlidrLessNum = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageSliderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SliderLayout高度
        imageSlider = binding.imageSlider.apply {
            layoutParams.height = w / 16 * 9
        }
        imageSliderIndicatorLinearLayout = binding.imageSliderIndicatorLinearLayout

        //取得資料
        GlobalScope.launch{
            println("World!1 + ${Thread.currentThread().name}")

            var data = getData()
            println("World!2 + ${Thread.currentThread().name}")

            withContext(Dispatchers.Main) {
                println("World!3 + ${Thread.currentThread().name}")
                addIndicator(imageSliderIndicatorLinearLayout,data.count())
                data.forEach { imageSliderObj ->
                    //slider object
                    TextSliderView(this@ImageSliderActivity).apply {
                        //文字
                        description(imageSliderObj.title)
                        //圖片的比例類型。
                        scaleType = BaseSliderView.ScaleType.Fit
                        //點選圖片事件
                        setOnSliderClickListener {
                            Toast.makeText(
                                this@ImageSliderActivity, imageSliderObj.title,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        //圖片及預設圖片
                        image(imageSliderObj.imageUrl).empty(R.mipmap.image_default)
                    }.also {
                        //加入到 SliderLayout
                        imageSlider.addSlider(it)
                    }
                }

                //SliderLayout增加 indicator layout
                //imageSlider.setCustomIndicator(imageSliderIndicator)

                imageSlider.apply {
                    //開始的圖片位置
                    setCurrentPosition(currentIndicatorPosition, true)
                    //delay => 第一次執行滑動時間
                    //duration => 每次滑動的時間
                    //autoRecover => false 使用者手指停留在該圖片時，則會停留在該圖片，不會繼續在滑動
                    startAutoCycle(3000L, 3000L, true)
                }

                //只有一張圖則不輪播
                if(data.count() < imageSlidrLessNum)imageSlider.stopAutoCycle()

                imageSlider.addOnPageChangeListener(object: ViewPagerEx.OnPageChangeListener{
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        //TODO("Not yet implemented")
                    }

                    override fun onPageSelected(position: Int) {
                        Log.d(TAG, "onPageSelected: $position")
                        changeIndicator(position,currentIndicatorPosition)
                        currentIndicatorPosition = position
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        //TODO("Not yet implemented")
                    }

                })
                
            }
        }
    }

    private fun addIndicator(imageSliderIndicatorLinearLayout: LinearLayoutCompat, count: Int) {

        if(count < imageSlidrLessNum)imageSliderIndicatorLinearLayout.visibility = View.GONE

        val indicateWidth = (w - 350)/count

        (0 until count).forEach { i ->
            var image = ImageView(this@ImageSliderActivity)
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

    /**
     *
     * 改變image slider indicator 圖示
     * */
    private fun changeIndicator(currentIndicate:Int,beforeIndicate:Int = -1){
        imageSliderIndicatorLinearLayout.getChildAt(currentIndicate).setBackgroundResource(R.drawable.selected)
        if(beforeIndicate != -1)imageSliderIndicatorLinearLayout.getChildAt(beforeIndicate).setBackgroundResource(R.drawable.unselected)
    }

    private suspend fun getData(): ArrayList<ImageSliderObj> {

        return  withContext(Dispatchers.IO) {

            delay(2000)

            ArrayList<ImageSliderObj>().apply {

                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698748147_66456.jpg",
                        "高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個 成長突飛猛進1"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747935_57304.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進2"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747945_53957.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進3"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747955_14884.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進4"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698748147_66456.jpg",
                        "高山峰親曝給兒子吃這個高山峰親曝給兒5"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747935_57304.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進6"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747945_53957.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進7"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747955_14884.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進8"
                    )
                )

            }
        }
    }

}