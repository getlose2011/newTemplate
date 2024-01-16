package com.example.newstemplate.service


import com.example.newstemplate.Model.CoverTopicModel
import com.example.newstemplate.Model.FlashNewsModel
import com.example.newstemplate.Model.ResponseModel
import com.example.newstemplate.Model.NewsSliderImageModel
import com.example.newstemplate.Model.TabCategoryModel
import com.example.newstemplate.libraries.Generic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class HomeListService {


    /**
     * 首頁輪播
     *
     * */
    suspend fun getNewsSliderImageApi(): ResponseModel<ArrayList<NewsSliderImageModel>> {
        return withContext(Dispatchers.IO) {
            delay(Generic.random1000To4000)
            ResponseModel<ArrayList<NewsSliderImageModel>>().apply {
                success = true
                message = ""
                data = ArrayList<NewsSliderImageModel>().apply {
                    add(
                        NewsSliderImageModel(
                            "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698748147_66456.jpg",
                            "1高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個 成長突飛猛進1"
                        )
                    )
                    add(
                        NewsSliderImageModel(
                            "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747935_57304.jpg",
                            "2高山峰親曝給兒子吃這個 成長突飛猛進2"
                        )
                    )
                    add(
                        NewsSliderImageModel(
                            "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747945_53957.jpg",
                            "3高山峰親曝給兒子吃這個 成長突飛猛進3"
                        )
                    )
                    add(
                        NewsSliderImageModel(
                            "https://img.news.ebc.net.tw/EbcNews/news/2024/01/05/1704472623_55845.jpg",
                            "4疑收中方資金參選立委！ 前民眾黨桃園黨部發言人馬治薇遭聲押"
                        )
                    )
                    add(
                        NewsSliderImageModel(
                            "https://img.news.ebc.net.tw/EbcNews/news/2024/01/05/1704474375_79604.jpg",
                            "5張誌家猝逝「2前妻靈前陪伴」 哥曝改名內幕"
                        )
                    )
                    add(
                        NewsSliderImageModel(
                            "https://img.news.ebc.net.tw/EbcNews/news/2024/01/05/1704474798_62173.jpg",
                            "6未接種XBB！羅一鈞：保護力恐歸零 1族群更要注意"
                        )
                    )
                }
            }
        }
    }

    /**
     * 快迅
     *
     * */
    suspend fun getFlashNewsApi(): ResponseModel<ArrayList<FlashNewsModel>> {
        return withContext(Dispatchers.IO) {
            delay(Generic.random1000To4000)
            ResponseModel<ArrayList<FlashNewsModel>>().apply {
                success = true
                message = ""
                data = ArrayList<FlashNewsModel>().apply {
                    add(FlashNewsModel(1,"下垂暗沉蠟黃皺紋找上門？不是年齡問題而是「糖化臉」",""))
                    add(FlashNewsModel(2,"快訊每天喝咖啡有益健康？營養師示警3類人要小心",""))
                    add(FlashNewsModel(3,"沒船沒飛機！金門人返鄉投票卡關 他1方案比投訴還快","https://news.ebc.net.tw/news/living/399860"))
                }
            }
        }
    }



    /**
     * 快訊
     *
     * */
    suspend fun getCoverTopicApi(): ResponseModel<ArrayList<CoverTopicModel>> {
        return withContext(Dispatchers.IO) {
            delay(Generic.random1000To4000)
            ResponseModel<ArrayList<CoverTopicModel>>().apply {
                success = true
                message = ""
                data = ArrayList<CoverTopicModel>().apply {
                    add(CoverTopicModel(1,
                        "下垂暗沉蠟黃皺紋找上門？不是年齡問題而是「糖化臉」",
                        "https://img.news.ebc.net.tw/EbcNews/news/2024/01/16/1705415086_60318.jpg",
                        "https://news.ebc.net.tw/news/living/39986"))
                    add(
                        CoverTopicModel(
                            2,
                            "快訊每天喝咖啡有益健康？營養師示警3類人要小心",
                            "https://img.news.ebc.net.tw/EbcNews/news/2024/01/16/1705404012_97101.jpg",
                            "https://news.ebc.net.tw/news/living/39860"))
                    add(CoverTopicModel(3,
                        "沒船沒飛機！金門人返鄉投票卡關 他1方案比投訴還快",
                        "https://img.news.ebc.net.tw/EbcNews/news/2024/01/16/1705412756_92355.jpg",
                        "https://news.ebc.net.tw/news/living/399860"))
                }
            }
        }
    }
}