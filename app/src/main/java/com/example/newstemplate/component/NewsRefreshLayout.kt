package com.example.newstemplate.component

import android.content.Context
import android.util.AttributeSet

class NewsRefreshLayout(context: Context, attrs: AttributeSet) : androidx.swiperefreshlayout.widget.SwipeRefreshLayout(context, attrs) {

    init {
       // setColorSchemeResources(R.color.colorPrimary)      //定義更新圖標顏色
        isEnabled = true      //預設關閉下拉更新功能
    }
}