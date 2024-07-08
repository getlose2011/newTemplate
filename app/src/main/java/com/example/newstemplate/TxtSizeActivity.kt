package com.example.newstemplate


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newstemplate.databinding.ActivityTxtSizeBinding
import com.warkiz.tickseekbar.OnSeekChangeListener
import com.warkiz.tickseekbar.SeekParams
import com.warkiz.tickseekbar.TickSeekBar


//所有字級大小及progress bar 對應的點
sealed class FontSize {
    data class Small(val type: String = "小", val position: Float=0f) : FontSize()
    data class Medium(val type: String = "中", val position: Float=33f) : FontSize()
    data class Larger(val type: String = "大", val position: Float=67f) : FontSize()
    data class XLarge(val type: String = "特大", val position: Float=100f) : FontSize()
}

//套件 => https://github.com/warkiz/TickSeekBar
class TxtSizeActivity : AppCompatActivity() {
    private val TAG = "TxtSizeActivity"
    private lateinit var binding: ActivityTxtSizeBinding
    private lateinit var webView: WebView
    //預設所點的字型會影響progress bar 的位置
    private var defaultFontType: String = FontSize.Larger().type

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTxtSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObsev()
        initData()

    }

    private fun initObsev() {

        //listener progress bar change
        binding.txtSizeSeekbar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                Log.i(TAG, seekParams.seekBar.toString())
                Log.i(TAG, seekParams.progress.toString())
                Log.i(TAG, seekParams.progressFloat.toString())
                Log.i(TAG, seekParams.fromUser.toString())
                Log.i(TAG, seekParams.thumbPosition.toString())
                Log.i(TAG, seekParams.tickText)
                //所選的字型並改變字型大小
                changeFontSize(seekParams.tickText)
                //改變webview字型大小
                getWebViewFontSize(seekParams.tickText)
            }

            override fun onStartTrackingTouch(seekBar: TickSeekBar) {}
            override fun onStopTrackingTouch(seekBar: TickSeekBar) {}
        }
    }

    private fun initData() {
        getWebViewFontSize(defaultFontType)
    }

    private fun initView() {

        //progress bar 每個點要呈現的文字
        val arr = arrayOf(
            FontSize.Small().type,
            FontSize.Medium().type,
            FontSize.Larger().type,
            FontSize.XLarge().type
        )

        binding.txtSizeSeekbar.apply {
            tickCount = arr.size
            //progress 呈現底下的文字
            customTickTexts(arr)
            //progress bar 停留的位置
            setProgress(getProgress(defaultFontType))
        }

        binding.txtSizeTxv.apply {
            //預設字型大小
            changeFontSize(defaultFontType)
        }

        //init webview
        webView = binding.txtSizeWebView.apply {
            settings.textZoom = 100
            setWebViewDefaultConfig(this)
            //loadUrl("https://labm3.news.ebc.net.tw/news/article/330242")
            setWebViewUrl()
            webViewClient = WebViewClient()
        }

    }

    private fun setWebViewUrl() {


        binding.txtSizeWebView.loadUrl("https://labm3.news.ebc.net.tw/news/article/330242")
    }

    //設定webview字型大小
    private fun getWebViewFontSize(s:String) {
        webView.settings.textZoom = when(s){
            FontSize.Small().type-> 80
            FontSize.Larger().type->120
            FontSize.XLarge().type->140
            else -> 100
        }
    }

    //取得progress bar 要停留的point
    private fun getProgress(s:String): Float {
        return when(s){
            FontSize.Small().type->FontSize.Small().position
            FontSize.Larger().type->FontSize.Larger().position
            FontSize.XLarge().type->FontSize.XLarge().position
            else -> FontSize.Medium().position
        }
    }

    //改變textview字型大小
    private fun changeFontSize(s:String){

        val defaultSize = when(s){
            FontSize.Small().type->resources.getDimension(R.dimen.txtSizeFontSmall)
            FontSize.Larger().type->resources.getDimension(R.dimen.txtSizeFontLarge)
            FontSize.XLarge().type->resources.getDimension(R.dimen.txtSizeFontXLarge)
            else -> resources.getDimension(R.dimen.txtSizeFontMedium)
        }

        binding.txtSizeTxv.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize)
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

}