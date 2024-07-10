package com.example.newstemplate.libraries

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import android.view.WindowMetrics
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.newstemplate.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


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

    /**
     * 設定 ViewPager 連結 Adapter
     * 若已有 adapter 則通知資料更新
     *
     * @param viewPager ViewPager
     * @param adapter PagerAdapter
     */
    fun setViewConnectAdapterOrNotify(viewPager: androidx.viewpager.widget.ViewPager, adapter: androidx.viewpager.widget.PagerAdapter) {
        if (viewPager.adapter == null) {
            viewPager.adapter = adapter
        } else {
            adapter.notifyDataSetChanged()
        }
    }

    val random1000To4000:Long = (1000..4000).random().toLong()
    val trueOrFalse:Boolean = (1..10).random()%2==0

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

    /**
     * 設定 RecyclerView 連結 Adapter
     * 若已有 adapter 則通知資料更新
     *
     * @param recyclerView RecyclerView
     * @param adapter RecyclerView.Adapter<*>
     */
    fun setViewConnectAdapterOrNotify(recyclerView: androidx.recyclerview.widget.RecyclerView, adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
        } else {
            adapter.notifyDataSetChanged()
        }
    }

     fun requestCallback(resultCallback: ((isSuccess: Boolean, errMessage: String?) -> Unit)? = null) {
         // Simulating some async operation
         Handler(Looper.getMainLooper()).postDelayed({
             // Example callback usage:
             resultCallback?.invoke(true, "Success")
         }, 10000)
    }


    /**
     * 設定 WebView 與 ProgressBar 預設設定
     *
     * @param webView          WebView 元件
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun setWebViewDefaultConfig(webView: WebView) {

        webView.apply {
            //webView 在 nestedScrollView 內開啟此功能會出錯
            //api19 以上開啟 Chromium 硬體加速功能
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            //setLayerType(View.LAYER_TYPE_HARDWARE, null)

            settings.apply {
                useWideViewPort = true     //開啟 Html Meta 功能
                loadWithOverviewMode = true        //自動適應裝置螢幕大小
                javaScriptEnabled = true       //啟用 Javascript 支援
                javaScriptCanOpenWindowsAutomatically = true     //啟用 Javascript 可開啟視窗支援
                //setAppCacheEnabled(true)      //開啟 Application H5 Caches 功能
                cacheMode = WebSettings.LOAD_NO_CACHE      //強制不從快取讀取資料
                builtInZoomControls = true       //開啟內部縮放功能
                displayZoomControls = false      //關閉系統縮放控制項
                domStorageEnabled = true        //開啟數據儲存(LocalStorage)
                loadsImagesAutomatically = true     //自動加載圖片
                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)
            }

            //allow cookie access
            CookieManager.getInstance().setAcceptCookie(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW      //允許網頁內容混和模式
                CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)      //允許第三方存取 cookie
            }
        }
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "example_channel_id"
            val channelName = "Example Channel"
            val channelDescription = "This is an example notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



}