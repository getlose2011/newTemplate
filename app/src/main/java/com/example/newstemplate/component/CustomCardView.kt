package com.example.newstemplate.component

import android.content.Context
import android.util.AttributeSet
import com.example.newstemplate.R

class CustomCardView(context: Context, attrs: AttributeSet) : androidx.cardview.widget.CardView(context, attrs) {

    init {
        preventCornerOverlap = false
        useCompatPadding = true
        radius = resources.getDimension(R.dimen.baseCardViewRadius)
    }
}
