package com.example.newstemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newstemplate.databinding.ActivityIconFontBinding

class IconFontActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIconFontBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIconFontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //code
        //val font = Typeface.createFromAsset(assets,"icomoon.ttf")
        //binding.navigatorTxt.apply {
        //    typeface = font
        //     text = resources.getString(R.string.iconFontSmile)
        // }

        binding.iconFontTxt.textSize = 200f
    }
}