package com.example.newstemplate.service


import com.example.newstemplate.Model.ResponseModel
import com.example.newstemplate.Model.TabCategoryModel
import com.example.newstemplate.libraries.Generic.random1000To4000
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class HomeService {


    /**
     * tab 分類資料api
     * */
     suspend fun getCategoryDataFromApi(): ResponseModel<ArrayList<TabCategoryModel>> {
        //取得資料
        return withContext(Dispatchers.IO) {
            delay(random1000To4000)
            //trueOrFalse
            ResponseModel<ArrayList<TabCategoryModel>>().apply {
                success = true
                message = ""
                data = ArrayList<TabCategoryModel>().apply {
                    add(TabCategoryModel("最新", -1))
                    add(TabCategoryModel("熱門", -2))
                    add(TabCategoryModel("政治", 4))
                    add(TabCategoryModel("生活", 3))
                    add(TabCategoryModel("健康", 2))
                }
            }
        }
    }
}