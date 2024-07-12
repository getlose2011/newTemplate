package com.example.newstemplate.service

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.newstemplate.Model.ExoPlayerModel
import com.example.newstemplate.Model.ResponseModel
import com.example.newstemplate.Model.TabCategoryModel
import com.example.newstemplate.libraries.Generic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExoPlayerApi {

    /**
     * tab 分類資料api
     * */
    suspend fun getData(): ResponseModel<ArrayList<ExoPlayerModel>> {

        //取得資料
        return withContext(Dispatchers.IO) {
            delay(Generic.random1000To4000)
            //trueOrFalse
            ResponseModel<ArrayList<ExoPlayerModel>>().apply {
                success = true
                message = ""
                data = ArrayList<ExoPlayerModel>().apply {
                    add(ExoPlayerModel("https://img.news.ebc.net.tw/EbcNews/audio/430801.mp3", 430801))
                    add(ExoPlayerModel("https://img.news.ebc.net.tw/EbcNews/audio/430802.mp3", 430802))
                    add(ExoPlayerModel("https://img.news.ebc.net.tw/EbcNews/audio/430803.mp3", 430803))
                    add(ExoPlayerModel("https://img.news.ebc.net.tw/EbcNews/audio/430804.mp3", 430804))
                    add(ExoPlayerModel("https://img.news.ebc.net.tw/EbcNews/audio/430805.mp3", 430805))
                }
            }
        }
    }

}