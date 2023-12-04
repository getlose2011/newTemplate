package com.example.newstemplate.libraries


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.window.layout.WindowMetricsCalculator
import java.time.Duration


object Generic  {

    /**
     *
     * 取得螢幕寬的PX
     * */
    @SuppressLint("SuspiciousIndentation")
    fun widthPx(activity: Activity):Int
        {
            val windowMetrics: androidx.window.layout.WindowMetrics =
                WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
                return windowMetrics.bounds.width()
        }

    /**
     *
     * 取得螢幕長的PX
     * */
    fun heightPx(activity: Activity):Int
    {
        val windowMetrics: androidx.window.layout.WindowMetrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
        return windowMetrics.bounds.height()
    }

    fun scaleAnimFun(
        fromScale: Float,
        toScale: Float,
        pivotXValue:Float=0f,
        pivotYValue:Float=0f,
        duration: Long = 300L
    ):ScaleAnimation =
        ScaleAnimation(
            fromScale, toScale,
            fromScale, toScale,
            Animation.RELATIVE_TO_SELF, pivotXValue,
            Animation.RELATIVE_TO_SELF, pivotYValue
        ).apply {
                setDuration(duration)
        }

}