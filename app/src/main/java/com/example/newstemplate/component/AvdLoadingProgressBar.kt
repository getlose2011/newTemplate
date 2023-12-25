package com.example.newstemplate.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.newstemplate.R

class AvdLoadingProgressBar  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val avd = AnimatedVectorDrawableCompat.create(context, R.drawable.play_white)!!

    init {
        // Set the AnimatedVectorDrawableCompat as the image drawable
        setImageDrawable(avd)

        // Register an animation callback to restart the animation when it ends
        avd.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                post { avd.start() }
            }
        })

        // Start the animation
        avd.start()
    }
}