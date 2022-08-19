package com.lie.puremusic.music.netease

import android.content.Context
import com.google.gson.Gson
import com.lie.puremusic.StaticData
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.DailyRecommendSongData
import com.lie.puremusic.music.netease.data.SongUrlData
import com.lie.puremusic.utils.MagicHttp
import okhttp3.FormBody

object RecommendSong {

    /**
     * 获取日推
     */
    fun getRecommendSong(context: Context, success: (DailyRecommendSongData) -> Unit) {
        val requestBody = FormBody.Builder()
            .add("crypto", "api")
            .add("cookie", StaticData.cookie)
            .add("withCredentials", "true")
            .build()

        MagicHttp.OkHttpManager().newPost("$API_DEFAULT/recommend/songs", requestBody, {
            try {
                val data = Gson().fromJson(it, DailyRecommendSongData::class.java)
                success.invoke(data)
            } catch (e: Exception) {
            }
        }, {

        })


    }

}