package com.example.newstemplate.component


import android.content.Context
import android.util.AttributeSet
import com.example.newstemplate.R
import kotlin.math.roundToInt

/**
 * 自定義 16:9,4:3,2:1 比率的 ImageView 類別
 */
class CustomImageView : androidx.appcompat.widget.AppCompatImageView {

    private val TAG = "CustomImageView"
    private var ratio:Float = 0f
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        customRatio(context,attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        customRatio(context,attrs)
    }
    private fun customRatio(context: Context, attrs: AttributeSet) {
        //custom attrs
        val customAttr = context.obtainStyledAttributes(attrs, R.styleable.CustomRatioImage)
        try{
            ratio = when(customAttr.getString(R.styleable.CustomRatioImage_scale)){
                resources.getString(R.string.ratio_4_3)-> .75f
                resources.getString(R.string.ratio_2_1)-> .5f
                resources.getString(R.string.ratio_16_9)-> .5625f
                else -> ratio
            }
            scaleType = ScaleType.CENTER_CROP
        }catch (e:Exception){
        }
        customAttr.recycle()
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = (width * ratio).roundToInt()
        setMeasuredDimension(width, height)
    }
}