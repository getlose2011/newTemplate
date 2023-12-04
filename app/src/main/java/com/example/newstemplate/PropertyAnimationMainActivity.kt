package com.example.newstemplate

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newstemplate.databinding.ActivityPropertyAnimationMainBinding


class PropertyAnimationMainActivity : AppCompatActivity() {

    private val TAG = "PropertyAnimationMainActivity"
    private lateinit var binding: ActivityPropertyAnimationMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyAnimationMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.propertyAnimationXmlBtn.setOnClickListener {
            AnimatorInflater.loadAnimator(this, R.animator.anim_color)
                .apply {
                    setTarget(binding.propertyAnimationTv)
                    start()
                }
        }

        binding.propertyAnimationSetBtn.setOnClickListener {
            AnimatorInflater.loadAnimator(this, R.animator.anim_set)
                .apply {
                    setTarget(binding.propertyAnimationTv)
                    start()
                }
        }

        binding.propertyAnimationCodeBtn.setOnClickListener {
            val anim = ObjectAnimator.ofFloat(binding.propertyAnimationTv, "rotation", 0.0f, 270.0f)
            anim.duration = 1000
            anim.start()
        }

        binding.propertyAnimationValueBtn.setOnClickListener {
            //建立一個100.0~0.0之間漸變的動畫
            val animation = ValueAnimator.ofFloat(100f, 0f)
            //監聽動畫值的改變
            animation.addUpdateListener { animation ->
                //在這裡取得從100.0~0之間的每個值
                binding.propertyAnimationTv.x = animation.animatedValue as Float
                binding.propertyAnimationTv.requestLayout()
            }
            //動畫期間
            animation.duration = 2000
            animation.start()
        }
    }
}