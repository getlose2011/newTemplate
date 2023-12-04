package com.example.newstemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newstemplate.component.CustomSelector
import com.example.newstemplate.component.ICustomSelector
import com.example.newstemplate.databinding.ActivityCustomSelectorBinding

class CustomSelectorActivity  : AppCompatActivity() {
    private val TAG = "CustomSelectorActivity"
    private lateinit var binding: ActivityCustomSelectorBinding
    private lateinit var customSelector: CustomSelector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customSelector = binding.selectDialog
        customSelector.setListerner(object:ICustomSelector{
            override fun click() {
                Toast.makeText(this@CustomSelectorActivity,"click",Toast.LENGTH_LONG).show()
            }

        })
    }
}