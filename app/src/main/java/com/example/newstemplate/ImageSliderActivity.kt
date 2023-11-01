package com.example.newstemplate

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
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
    private lateinit var imageSliderIndicator: PagerIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageSliderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //螢幕寬
        val w = Generic.widthPx(this@ImageSliderActivity)

        //SliderLayout高度
        imageSlider = binding.imageSlider.apply {
            layoutParams.height = w / 16 * 9
        }

        imageSliderIndicator = binding.imageSliderIndicator

        //取得資料
        GlobalScope.launch {

            delay(1000)
            var data = getData()

            withContext(Dispatchers.Main) {
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
                imageSlider.setCustomIndicator(imageSliderIndicator)

                imageSlider.apply {
                    //開始的圖片位置
                    setCurrentPosition(2, true)
                    //delay => 第一次執行滑動時間
                    //duration => 每次滑動的時間
                    //autoRecover => false 使用者手指停留在該圖片時，則會停留在該圖片，不會繼續在滑動
                    startAutoCycle(3000L, 3000L, true)
                }

            }
        }
    }


    private  fun getData(): ArrayList<ImageSliderObj> {       
       
            return  ArrayList<ImageSliderObj>().apply {
                add(ImageSliderObj("https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698748147_66456.jpg","高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個 成長突飛猛進1"))
                add(ImageSliderObj("https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747935_57304.jpg","高山峰親曝給兒子吃這個 成長突飛猛進2"))
                add(ImageSliderObj("https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747945_53957.jpg","高山峰親曝給兒子吃這個 成長突飛猛進3"))
                add(ImageSliderObj("https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747955_14884.jpg","高山峰親曝給兒子吃這個 成長突飛猛進4"))
            
        }

    }

}