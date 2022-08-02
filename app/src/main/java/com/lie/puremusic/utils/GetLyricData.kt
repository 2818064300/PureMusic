package com.lie.puremusic.utils

import com.lie.puremusic.StaticData
import com.lie.puremusic.pojo.Lyrics
import com.lie.puremusic.pojo.Song
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class GetLyricData : Runnable {
    private var songs: Song?
    private var id: String?

    constructor(songs: Song?, id: String?) {
        this.songs = songs
        this.id = id
    }

    override fun run() {
        val request = if (StaticData.Root.equals("网易云音乐")) {
            Request.Builder()
                .url("http://www.puremusic.com.cn:3000/lyric?id=$id")
                .addHeader("cookie", StaticData.cookie)
                .build()
        } else {
            Request.Builder()
                .url("http://www.puremusic.com.cn:3300/lyric?songmid=$id")
                .build()
        }
        val response = OkHttpClient().newCall(request).execute()
        val jsonObject = JSONObject(response.body!!.string())
        if (StaticData.Root.equals("网易云音乐")) {
            songs?.setLyric(Lyrics(jsonObject.getJSONObject("lrc").getString("lyric")))
        } else {
            songs?.setLyric(Lyrics(jsonObject.getJSONObject("data").getString("lyric")))
        }
    }
}