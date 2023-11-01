package com.example.newstemplate.libraries


import android.annotation.SuppressLint
import android.app.Activity
import androidx.window.layout.WindowMetricsCalculator


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
}