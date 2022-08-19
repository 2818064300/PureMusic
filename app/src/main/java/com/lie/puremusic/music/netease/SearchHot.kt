package com.lie.puremusic.music.netease

import com.google.gson.Gson
import com.lie.puremusic.api.CloudMusicApi
import com.lie.puremusic.music.netease.data.SearchHotData
import com.lie.puremusic.utils.MagicHttp

object SearchHot {
    fun getSearchHot(success: (SearchHotData) -> Unit) {
        val url = CloudMusicApi.SEARCH_HOT_DETAIL
        MagicHttp.OkHttpManager().newGet(url, {
            try {
                val searchHotData = Gson().fromJson(it, SearchHotData::class.java)
                if (searchHotData.code == 200) {
                    success.invoke(searchHotData)
                }
            } catch (e: Exception) {

            }
        }, {

        })
    }
}