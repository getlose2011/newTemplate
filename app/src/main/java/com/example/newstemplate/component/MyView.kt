package com.example.newstemplate.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class MyCustomView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var paint = Paint()
    private var rectangleColor = Color.BLUE // Initial color
    private var i = 0f

    init {
        init()
    }

    private fun init() {
        paint.color = rectangleColor
        paint.strokeWidth = 8F
        paint.style = Paint.Style.STROKE
    }

    fun changeRectangleColor() {
        // Notify the system that the view needs to be redrawn
        invalidate()

        // Change the color of the rectangle
        rectangleColor = if (rectangleColor == Color.BLUE) {
            Color.RED
        } else {
            Color.BLUE
        }

        paint.color = rectangleColor
    }

    override fun onDraw(canvas: Canvas) {
        i += 20
        val height = height
        canvas.drawRect(0f, 0f, 10f, 10f, paint)
        canvas.drawRect(i, height-205f, 70f, height-15f, paint)
    }
}