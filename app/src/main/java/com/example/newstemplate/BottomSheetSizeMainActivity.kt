package com.example.newstemplate

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.newstemplate.databinding.ActivityBottomSheetBinding
import com.example.newstemplate.databinding.ActivityBottomSheetSizeMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.warkiz.tickseekbar.OnSeekChangeListener
import com.warkiz.tickseekbar.SeekParams
import com.warkiz.tickseekbar.TickSeekBar

class BottomSheetSizeMainActivity : AppCompatActivity() {

    private val TAG = "BottomSheetSizeMainActivity"
    private lateinit var binding: ActivityBottomSheetSizeMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomSheetSizeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sizeBtn.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_text_resizing, null)

            //listener
            //progress bar 每個點要呈現的文字
            val arr = arrayOf(
                FontSize.Small().type,
                FontSize.Medium().type,
                FontSize.Larger().type,
                FontSize.XLarge().type
            )

            val dialog = BottomSheetDialog(this)
            //dialog.behavior.peekHeight = h
            dialog.setContentView(dialogView)
            //dialogView.layoutParams.height = h

            dialog.behavior.isDraggable = false
            dialog.setCanceledOnTouchOutside(false)

            dialogView.findViewById<Button>(R.id.bottomSheetSizeBtn)?.apply {
                setOnClickListener {

                    dialog.dismiss()

                }
            }

            dialogView.findViewById<TickSeekBar>(R.id.bottomSheetSizeSeekbar)?.apply {
                tickCount = arr.size
                //progress 呈現底下的文字
                customTickTexts(arr)
                //progress bar 停留的位置
                setProgress(88f)

                onSeekChangeListener = object :  OnSeekChangeListener {
                    override fun onSeeking(seekParams: SeekParams) {
                        Log.i(TAG, seekParams.seekBar.toString())
                        Log.i(TAG, seekParams.progress.toString())
                        Log.i(TAG, seekParams.progressFloat.toString())
                        Log.i(TAG, seekParams.fromUser.toString())
                        Log.i(TAG, seekParams.thumbPosition.toString())
                        Log.i(TAG, seekParams.tickText)
                        //所選的字型並改變字型大小
                        //changeFontSize(seekParams.tickText)
                        //改變webview字型大小
                        //getWebViewFontSize(seekParams.tickText)
                    }

                    override fun onStartTrackingTouch(seekBar: TickSeekBar) {}
                    override fun onStopTrackingTouch(seekBar: TickSeekBar) {}
                }
            }

            dialog.show()


        }
    }
}