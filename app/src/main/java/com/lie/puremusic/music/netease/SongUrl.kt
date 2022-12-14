package com.lie.puremusic.music.netease

import com.google.gson.Gson
import com.lie.puremusic.StaticData
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.SongUrlData
import com.lie.puremusic.utils.MagicHttp
import okhttp3.FormBody

object SongUrl {

    fun getSongUrlCookie(id: String,level:String, success: (SongUrlData.UrlData) -> Unit) {
        val requestBody = FormBody.Builder()
            .add("crypto", "api")
            .add("cookie", StaticData.cookie)
            .add("withCredentials", "true")
            .add("realIP", "211.161.244.70")
            .add("id", id)
            .add("level", level)
            .build()

        MagicHttp.OkHttpManager().newPost("$API_DEFAULT/song/url/v1", requestBody, {
            try {
                val songUrlData = Gson().fromJson(it, SongUrlData::class.java)
                success.invoke(songUrlData.data[0])
            } catch (e: Exception) {
            }
        }, {

        })
    }
}