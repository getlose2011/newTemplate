package com.example.newstemplate.component

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.widget.LinearLayout
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import com.example.newstemplate.R
import com.example.newstemplate.libraries.Generic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

interface ICustomSelector{
    fun click()
}

//RelativeLayout
class CustomSelector(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var isOpen = false
    private var rotateValue = 0f
    private val view by lazy {
        View.inflate(context, R.layout.custom_selector, this)
    }
    private lateinit var listerner: ICustomSelector
    private lateinit var circleIv: ImageView
    private lateinit var dialogLinearlayout: LinearLayout
    private lateinit var iconLinearLayout: LinearLayout
    private val duration = 350L

    init {
        component()
        observe()
        setting()
    }

    fun setListerner(listener: ICustomSelector){
        listerner = listener
    }

    private fun observe() {
        circleIv.setOnClickListener {
            //listerner.click()

            val f = rotateValue
            val t = f - 50f

            val anim = ObjectAnimator.ofFloat(circleIv, "rotation",
                if(!isOpen)
                    0.0f
                else
                    -50f,
                if(!isOpen)
                    -50f
                else
                    0f
            )

            displayDialog(!isOpen)

            anim.duration = duration
            anim.start()

            isOpen = !isOpen
        }
    }

    private fun component() {
        dialogLinearlayout = view.findViewById(R.id.dialog_select_linearlayout)
        iconLinearLayout = view.findViewById(R.id.iconLinearLayout)
        circleIv = view.findViewById(R.id.select_imageview)
    }

    private fun setting() {
        drawCircle()
        drawDialog()
        displayDialog(false)
    }

    private fun displayDialog(showDialog: Boolean) {
        //開啟、關閉Dialog
        var fromScale = 0.0f
        var toScale = 1.0f

        if(showDialog){
            dialogLinearlayout.visibility = View.VISIBLE
            dialogLinearlayout.bringToFront()
        }else{
            //由大變小縮起
            fromScale = 1.0f
            toScale = 0.0f
            dialogLinearlayout.visibility = View.GONE
        }

        //Scale動畫，顯示時由小放大，關閉時由大放小
        dialogLinearlayout.startAnimation(
            Generic.scaleAnimFun(
                fromScale,
                toScale,
                0.5f,
                1f,
                duration = duration
            ))
    }

    private fun drawDialog() {

        //畫Dialog
        val triangleWidth = 50
        val triangleHeight = 40
        val dialogWidth = 500
        val dialogImagesHeight = getDialogHeight()
        val dialogHeight = dialogImagesHeight + triangleHeight
        //繪制Dialog的外觀
        val bitmap = Bitmap.createBitmap(500, dialogHeight, Bitmap.Config.RGB_565)

        // 產生Paint
        val p = Paint()
        p.strokeWidth = 2f
        p.color = Color.WHITE
        p.style = Paint.Style.FILL
        p.isAntiAlias = true
        p.setShadowLayer(5f, 2f, 2f, Color.LTGRAY)
        p.strokeJoin = Paint.Join.ROUND
        p.strokeCap = Paint.Cap.ROUND
        p.pathEffect = CornerPathEffect(20f)

        //畫Dialog
        val canvas = Canvas(bitmap)

        val leftX = 0
        val leftY = 0

        val centerX = (dialogWidth - leftX) / 2 + leftX

        val path = Path()

        path.moveTo(leftX.toFloat(), leftY.toFloat())
        path.lineTo(dialogWidth.toFloat(), leftY.toFloat())
        path.lineTo(dialogWidth.toFloat(), dialogImagesHeight.toFloat())
        path.lineTo((centerX + triangleWidth).toFloat(), dialogImagesHeight.toFloat())
        path.lineTo(centerX.toFloat(), (dialogImagesHeight + triangleHeight).toFloat())
        path.lineTo((centerX - triangleWidth).toFloat(), dialogImagesHeight.toFloat())
        path.lineTo(leftX.toFloat(), dialogImagesHeight.toFloat())
        path.close()
        canvas.drawPath(path, p)


        val drawable = BitmapDrawable(resources, bitmap)

        dialogLinearlayout.background = drawable
    }

    private fun getDialogHeight(): Int {
        var row = 2
        var rowHeight = 150
        var triangleHeight = 40
        val imageHeight = row * rowHeight
        val dialogHeight = imageHeight + triangleHeight



        val l = dialogLinearlayout()
        val l1 = dialogLinearlayout()



        iconLinearLayout.addView(l1)
        iconLinearLayout.addView(l)

        return dialogHeight
    }

    fun dialogLinearlayout():LinearLayout{
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = HORIZONTAL
        val imageParam = LinearLayout.LayoutParams(
            0,500 / 6,1f
        )

        val image = ImageView(context)

        image.setImageResource(R.drawable.fblike_select)

        linearLayout.addView(image,imageParam)

        val image1 = ImageView(context)

        image1.setImageResource(R.drawable.fblike_notselect)

        linearLayout.addView(image1,imageParam)

        val image2 = ImageView(context)

        image2.setImageResource(R.drawable.mark)

        linearLayout.addView(image2,imageParam)

        return linearLayout
    }

    private fun drawCircle() {
        val bitmap: Bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
        val p = Paint()
        p.color = Color.BLUE
        p.style = Paint.Style.FILL
        p.isAntiAlias = true

        val canvas = Canvas(bitmap!!)

        //circle
        canvas.drawCircle(25f, 25f, 25f,  p)
        //line
        p.color = Color.WHITE
        p.strokeWidth = 5f
        canvas.drawLine(25f,5f,25f,45f, p)
        canvas.drawLine(5f,25f,45f,25f, p)

        circleIv.setImageBitmap(bitmap)
    }

}