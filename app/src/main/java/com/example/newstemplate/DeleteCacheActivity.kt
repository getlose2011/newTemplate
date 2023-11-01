package com.example.newstemplate


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newstemplate.databinding.ActivityDeleteCacheBinding

class DeleteCacheActivity : AppCompatActivity() {

    private val TAG = "DeleteCacheActivity"
    private lateinit var binding: ActivityDeleteCacheBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteCacheBinding.inflate(layoutInflater)
        setContentView(binding.root)
		
        binding.deleteBtn.setOnClickListener{
            deleteCache()
            it.isEnabled = false
            Toast.makeText(this,"已清除",Toast.LENGTH_LONG).show()
        }
    }

	//Android清除快取
    private fun deleteCache(){
        MyApplication.mContext.cacheDir.deleteRecursively()
    }

}