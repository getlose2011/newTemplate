package com.example.newstemplate


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
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
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("https://www.yahoo.com.tw")
        }

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

}