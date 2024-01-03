package com.example.newstemplate

import android.animation.Animator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.newstemplate.databinding.ActivityAnimationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimationActivity : AppCompatActivity() {

    private val TAG = "AnimationActivity"
    private lateinit var binding: ActivityAnimationBinding
    private lateinit var arrow: ImageView
    private lateinit var frameLayout: FrameLayout
    private var arrowGap = 30

    private var sAnim: ObjectAnimator? = null
    private var eAnim: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrow = binding.animationBreakingArrow
        frameLayout = binding.animationFrameLayout



        GlobalScope.launch{
//取得framelayout寬,一開始取不到真實的寬，所以用post取得
            frameLayout.post {
                val w = frameLayout.width
                frameLayout.layoutParams.width = w+arrowGap
                frameLayout.requestLayout()
                sAnim = ObjectAnimator.ofFloat(arrow, "translationX", 0f,(0-arrowGap).toFloat() )
                eAnim = ObjectAnimator.ofFloat(arrow, "translationX", (0-arrowGap).toFloat(),0f )

                breakingArrowStart()
            }
            delay(400)

            withContext(Dispatchers.Main) {

                startSplash()

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

       // binding.animationSplashIv.clearAnimation()
        binding.animationSplashIv.clearAnimation()
        sAnim = null
        eAnim = null
    }

    fun startSplash(){
        //開機動畫
        val sAnim1 = AnimationUtils.loadAnimation(this@AnimationActivity, R.anim.scale_in)

        sAnim1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                //TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animation?) {
                //TODO("Not yet implemented")
                binding.animationSplashIv.startAnimation(AnimationUtils.loadAnimation(this@AnimationActivity, R.anim.scale_out))
            }

            override fun onAnimationRepeat(animation: Animation?) {
                //TODO("Not yet implemented")
            }

        })

        binding.animationSplashIv.apply {
            startAnimation(sAnim1)
        }
    }

    fun breakingArrowStart(){
        //val sAnim = ObjectAnimator.ofFloat(arrow, "translationX", 0f,(0-arrowGap).toFloat() )
        sAnim?.duration = 1200
        sAnim?.start()
        sAnim?.addListener(object: Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {
                //TODO("Not yet implemented")
            }
            override fun onAnimationEnd(animation: Animator) {
                //TODO("Not yet implemented")
                sAnim?.removeAllListeners()
                sAnim?.cancel()
                breakingArrowEnd()
            }
            override fun onAnimationCancel(animation: Animator) {
                //TODO("Not yet implemented")
            }
            override fun onAnimationRepeat(animation: Animator) {
                //TODO("Not yet implemented")
            }
        })
    }

    fun breakingArrowEnd(){
        //val eAnim = ObjectAnimator.ofFloat(arrow, "translationX", (0-arrowGap).toFloat(),0f )
        eAnim?.duration = 800
        eAnim?.start()
        eAnim?.addListener(object: Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {
                //TODO("Not yet implemented")
            }
            override fun onAnimationEnd(animation: Animator) {
                //TODO("Not yet implemented")
                eAnim?.removeAllListeners()
                eAnim?.cancel()
                breakingArrowStart()
            }
            override fun onAnimationCancel(animation: Animator) {
                //TODO("Not yet implemented")
            }
            override fun onAnimationRepeat(animation: Animator) {
                //TODO("Not yet implemented")
            }
        })
    }

}