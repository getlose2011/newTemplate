package com.example.newstemplate.component


import android.content.Context
import android.widget.ImageView
import kotlin.math.roundToInt
import android.annotation.SuppressLint
import android.util.AttributeSet

/**
 * 自定義 16:9 比率的 ImageView 類別
 */
@SuppressLint("AppCompatCustomView")
class CustomImageView(context: Context, attrs: AttributeSet) : ImageView(context,attrs) {



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        val height = (width * .5625f).roundToInt()

        setMeasuredDimension(width, height)


    }
}
