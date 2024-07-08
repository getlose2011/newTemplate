package com.example.newstemplate


import android.annotation.SuppressLint
import android.graphics.Outline
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.TextView
import com.example.newstemplate.databinding.ActivityBottomSheetBinding
import com.example.newstemplate.libraries.Generic
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.awaitCancellation

class BottomSheetActivity : AppCompatActivity() {
    private val TAG = "BottomSheetActivity"
    private lateinit var binding: ActivityBottomSheetBinding


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imageView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                outline?.setRoundRect(0, 0, view!!.width, (view.height+48f).toInt(), 48f)
            }
        }
        binding.imageView.clipToOutline = true


        val h = Generic.getScreenSize(this).height*4/5
        binding.bottomSheetBtn.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

            val dialog = BottomSheetDialog(this)
            dialog.behavior.peekHeight = h
            dialog.setContentView(dialogView)
            dialogView.layoutParams.height = h

            dialog.behavior.isDraggable = false
            dialog.setCanceledOnTouchOutside(false)

            dialogView.findViewById<Button>(R.id.cancelBtn).apply {

                setOnClickListener {
                    dialog.dismiss()
                }

            }

            dialog.show()

        }
    }



}