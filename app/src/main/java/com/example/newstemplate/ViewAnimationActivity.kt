package com.example.newstemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import com.example.newstemplate.databinding.ActivityViewAnimationBinding

class ViewAnimationActivity : AppCompatActivity() {

    private val TAG = "ViewAnimationActivity"
    private lateinit var binding: ActivityViewAnimationBinding
    private lateinit var valueAnimationFbIv: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        valueAnimationFbIv = binding.viewAnimationFbIv

        binding.viewAnimationXmlBtn.setOnClickListener {
            //使用xml方式
            binding.viewAnimationIv.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha))
        }

        binding.viewAnimationCodeBtn.setOnClickListener {
            //使用程式的方式
            //透明過至0.2
            val animation = AlphaAnimation(1.0f, 0f)
            animation.duration = 1000
            animation.repeatCount = 2
            //animation.repeatCount = Animation.INFINITE //重覆無限次
            animation.fillAfter = true//動畫結束後維持在結束狀態
            binding.viewAnimationIv.startAnimation(animation)
        }

        binding.viewAnimationSetBtn.setOnClickListener {
            val animSet = AnimationSet(true)
            animSet.fillAfter = true

            val alphaAnimation = AlphaAnimation(1.0f, 0f)
            alphaAnimation.duration = 1000

            val translateAnimation = TranslateAnimation(
                0f,
                -100f,
                0f,
                0f)
            translateAnimation.duration = 1000
            //translateAnimation.repeatCount = 2

            translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    println("動畫開始")
                }

                override fun onAnimationEnd(animation: Animation) {
                    println("動畫結束")

                    val translateAnimation1 = TranslateAnimation(
                        100f,
                        0f,
                        0f,
                        0f)
                    translateAnimation1.duration = 1000
                    binding.viewAnimationIv.startAnimation(translateAnimation1)
                }

                override fun onAnimationRepeat(animation: Animation) {
                    println("動畫重覆執行")
                }
            })
            //設定加速模式
            translateAnimation.interpolator = AccelerateInterpolator()
            //將scaleAnimation加入AnimationSet動畫組合
            animSet.addAnimation(alphaAnimation)
            //將translateAnimation加入AnimationSet動畫組合
            animSet.addAnimation(translateAnimation)

            binding.viewAnimationIv.startAnimation(animSet)
        }

        //https://innovationm.co/scale-animation-in-android-how-it-works/
        //https://ithelp.ithome.com.tw/articles/10202944
        var fbLike = false
        valueAnimationFbIv.setOnClickListener{
            if(!fbLike){
                binding.viewAnimationFbIv.setImageResource(R.drawable.fblike_select)
            }else{
                binding.viewAnimationFbIv.setImageResource(R.drawable.fblike_notselect)
            }

            val animSet = AnimationSet(true)

            val animRrotate = RotateAnimation(
                0f,
                -30f,
                RotateAnimation.RELATIVE_TO_SELF, //pivotXType
                0.5f, //設定x旋轉中心點
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f
            )
            //動畫持續時間
            animRrotate.duration = 200

            //步驟2：放大1.2倍的動畫
            val animScale = ScaleAnimation(
                1.0f, // x起始縮放比例
                1.2f, // x結束縮放比例
                1.0f, // x起始縮放比例
                1.2f, // y結束縮放比例
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 1f)
            animScale.duration = 200

            animSet.addAnimation(animRrotate)
            animSet.addAnimation(animScale)

            valueAnimationFbIv.startAnimation(animSet)

            fbLike = !fbLike
        }
    }
}