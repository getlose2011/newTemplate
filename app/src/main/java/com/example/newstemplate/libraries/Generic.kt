package com.example.newstemplate.libraries

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import android.view.WindowMetrics
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.annotation.RequiresApi


object Generic  {

    /**
     *
     * width or height pixels
     * */
    private val api: Api =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ApiLevel30()
        else Api()

    /**
     * Returns screen size in pixels.
     */
    fun getScreenSize(context: Context): Size = api.getScreenSize(context)

    @Suppress("DEPRECATION")
    private open class Api {
        open fun getScreenSize(context: Context): Size {
            val display = context.getSystemService(WindowManager::class.java).defaultDisplay
            val metrics = if (display != null) {
                DisplayMetrics().also { display.getRealMetrics(it) }
            } else {
                Resources.getSystem().displayMetrics
            }
            return Size(metrics.widthPixels, metrics.heightPixels)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private class ApiLevel30 : Api() {
        override fun getScreenSize(context: Context): Size {
            val metrics: WindowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
            return Size(metrics.bounds.width(), metrics.bounds.height())
        }
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