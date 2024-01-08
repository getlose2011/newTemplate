package com.example.newstemplate.component


import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.newstemplate.R


class MyTextView : AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        customFontIcon(context,attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        customFontIcon(context,attrs)
    }

    private fun customFontIcon(context: Context, attrs: AttributeSet) {

        //custom attrs
        val customAttr = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView)

        try{
            val ttfName = when(customAttr.getString(R.styleable.CustomFontTextView_ttfName)){
                resources.getString(R.string.genIconTTfName)-> "gene_icon"
                else->"icon"
            }

            //feedback
            isClickable = true

            //icon font path
            val customFont = Typeface.createFromAsset(context.assets, "font/$ttfName.ttf")
            typeface = customFont

        }catch (e:Exception){

        }
        customAttr.recycle()
    }
}