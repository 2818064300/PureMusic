package com.lie.puremusic.music.netease

import android.content.Context
import com.google.gson.Gson
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.TopListData
import com.lie.puremusic.utils.MagicHttp

/**
 * 排行榜
 */
object TopList {

    private const val API = "${API_DEFAULT}/toplist/detail"

    fun getTopList(context: Context, success: (TopListData) -> Unit) {
        MagicHttp.OkHttpManager().getByCache(context, API, {
            try {
                val topListData = Gson().fromJson(it, TopListData::class.java)
                if (topListData.code == 200) {
                    success.invoke(topListData)
                }
            } catch (e: Exception) {
            }
        }, {
        })
    }

}