package com.example.newstemplate.component

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newstemplate.R


class GridLayoutRecycler : RecyclerView {

    //一列有幾個
    private var spanCount = 2

    constructor(context: Context) : super(context){
        init(context,null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init(context,attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        if(attrs != null){
            val attri = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomGridLayoutRecycler,0,0)
            spanCount = attri.getInt(R.styleable.CustomGridLayoutRecycler_sCount,spanCount)
        }

        layoutManager = GridLayoutManager(context, spanCount)
    }


    override fun onMeasure(widthSpec: Int, heightSpec: Int) {

        val widthSpecMode = MeasureSpec.getMode(widthSpec)
        val widthSpecSize = MeasureSpec.getSize(widthSpec)
        val heightSpecMode = MeasureSpec.getMode(heightSpec)
        val heightSpecSize = MeasureSpec.getSize(heightSpec)

        if(widthSpecMode == MeasureSpec.AT_MOST){
            //wrap_content
            // MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        }else if(widthSpecMode == MeasureSpec.EXACTLY){
            //固定大小.ex:30dp,Match_Parent
        }else if(widthSpecMode == MeasureSpec.UNSPECIFIED){
            //大小未定
        }

        super.onMeasure(widthSpec, heightSpec)

    }

    //取得一列有幾個
    fun getColumNum():Int = spanCount

}