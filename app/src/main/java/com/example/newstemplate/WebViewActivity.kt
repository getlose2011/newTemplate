package com.example.newstemplate

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.example.newstemplate.databinding.ActivityWebViewBinding
import com.example.newstemplate.libraries.Generic


class WebViewActivity : AppCompatActivity() {

    private val TAG = "WebViewActivity_Tag"
    private lateinit var binding: ActivityWebViewBinding
    private lateinit var mProgressBar: ProgressBar
    private var mWebView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mWebView = binding.webView
        mProgressBar = binding.progressBar.apply {
            visibility = View.VISIBLE
        }
        //setSupportMultipleWindows(true)可搭配oncreatewindow
        //setSupportMultipleWindows(false)所有連結都在webview執行
        binding.webView.apply {
            settings.textZoom = 100
            Generic.setWebViewDefaultConfig(this)
            mWebView?.loadUrl("https://m3.news.ebc.net.tw/column/")
            webChromeClient = mWebChromeClient
            webViewClient = newsContentWebViewClient
        }
    }

    override fun onDestroy() {
        if (mWebView != null) {
            // 從父視圖中移除
            (mWebView?.parent as ViewGroup).removeView(mWebView)
            // 銷毀 WebView
            mWebView?.destroy()
            mWebView = null
        }
        super.onDestroy()
    }

    /**
     * 新聞內容頁 WebView WebViewClient
     * 判斷連接是否在當前的WebView中打開或開browser
     * 只有在webview點連結時才會觸發shouldOverrideUrlLoading
     * onPageFinished 在webview載入網頁後都會觸發
     */
    private var newsContentWebViewClient = object : WebViewClient() {

        //cound use WeakReference if leaking

        //load url
        @Suppress("OverridingDeprecatedMember")
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {

            Log.d(TAG, "shouldOverrideUrlLoading: $url")
            Log.d(TAG, "shouldOverrideUrlLoading: Uri ${Uri.parse(url)}")
            mProgressBar.visibility = View.VISIBLE
            webView.loadUrl(url)
            return true
        }

        //WebView 內容開始顯示
        override fun onPageCommitVisible(view: WebView, url: String) {
            //每次顯示內容時將畫面移至最上方
            //mNestedScrollView.scrollTo(0, 0)
        }

        //WebView 內容已全部加載完成(此時調用 JS 才能確保成功)
        //url => webview url載入的網址
        override fun onPageFinished(view: WebView?, url: String?) {

            Log.d(TAG, "onPageFinished: $url")

        }
    }

    /**
     * 新聞內容頁 WebView WebChromeClient
     */
    private var mWebChromeClient: WebChromeClient? = object: WebChromeClient(){
        //判斷target open a tag
        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {

            //開啟dialog新視窗
            /*
            val newWebView = WebView(this@WebViewActivity)
            val webSettings = newWebView.settings
            webSettings.javaScriptEnabled = true
            newWebView.webViewClient = WebViewClient()
            newWebView.webChromeClient = WebChromeClient()

            // 設置新的 WebView 的視圖

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@WebViewActivity)
            builder.setView(newWebView)
            builder.setPositiveButton("關閉") { dialog, which ->
                // 停止加載
                newWebView.stopLoading()
                // 清除歷史記錄
                newWebView.clearHistory()
                // 從父視圖中移除
                (newWebView.parent as ViewGroup).removeView(newWebView)
                // 銷毀 WebView
                newWebView.destroy()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()

            //// 使用 WebViewTransport 設置新的 WebView
            val transport = resultMsg!!.obj as WebViewTransport
            transport.webView = newWebView
            //發送消息，通知系統我們已經設置好了新的 WebView
            resultMsg.sendToTarget()

            return true
            */


            if (view?.context == null || resultMsg == null) {
                return false
            }

            // Extract the URL from the result message
            val newWebView = WebView(this@WebViewActivity).apply {
                settings.javaScriptEnabled = true
            }

            val transport = resultMsg.obj as? WebView.WebViewTransport
            transport?.webView = newWebView
            resultMsg.sendToTarget()

            newWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val url = request?.url.toString()
                   //openChromeCustomTabs(this@WebViewActivity, url)
                    return true
                }


            }

            return true





            /*


            if (view?.context == null) {
                return false
            }
            if (resultMsg == null) {
                return false
            }

            view.hitTestResult.extra?.let {url->
                //Generic.openChromeCustomTabs(requireActivity(), url)
                binding.progressBar.visibility = View.VISIBLE
                mWebView?.loadUrl(url)
            }
            return false

             */
        }

        //設定讀取進度條繫結
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            //Log.d("TAG", "onProgressChanged: $newProgress")
            if(newProgress == 100){
                mProgressBar.visibility = View.GONE
            }

            mProgressBar.progress = newProgress
        }

        override fun onCloseWindow(window: WebView?) {
            super.onCloseWindow(window)
            Log.d(TAG, "onCloseWindow: ")
        }


        /**
         * 使用 Chrome Custom Tabs 開啟外部瀏覽器
         *
         * @param context Context 物件
         * @param url     要開啟的網址字串
         */
        fun openChromeCustomTabs(context: Context, url: String) {
            try{
                CustomTabsIntent.Builder().setDefaultColorSchemeParams(
                    CustomTabColorSchemeParams.Builder().setToolbarColor(
                    ContextCompat.getColor(context, R.color.black)).build()).build().launchUrl(context, Uri.parse(url))
            } catch (e: ActivityNotFoundException) {
                Log.e("LOG_TAG", e.message ?: e.toString())
            }
        }
    }
}