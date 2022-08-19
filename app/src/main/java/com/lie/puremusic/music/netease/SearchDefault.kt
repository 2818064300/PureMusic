package com.lie.puremusic.music.netease

import com.google.gson.Gson
import com.lie.puremusic.api.CloudMusicApi
import com.lie.puremusic.music.netease.data.SearchDefaultData
import com.lie.puremusic.utils.MagicHttp

object SearchDefault {
    fun getSearchDefault(success: (SearchDefaultData) -> Unit) {
        val url = CloudMusicApi.SEARCH_DEFAULT
        MagicHttp.OkHttpManager().newGet(url, {
            try {
                val searchDefaultData = Gson().fromJson(it, SearchDefaultData::class.java)
                if (searchDefaultData.code == 200) {
                    success.invoke(searchDefaultData)
                }
            } catch (e: Exception) {

            }
        }, {

        })
    }
}