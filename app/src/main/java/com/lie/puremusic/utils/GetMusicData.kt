package com.lie.puremusic.utils

import android.content.Context
import com.lie.puremusic.StaticData
import com.lie.puremusic.pojo.Song
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.Executors

class GetMusicData : Runnable {
    private var songs: Song?
    private var id: String?
    private var type: String? = null
    private var context: Context? = null

    constructor(songs: Song?, id: String) {
        this.songs = songs
        this.id = id
    }

    constructor(songs: Song?, id: String, type: String?) {
        this.songs = songs
        this.id = id
        this.type = type
    }

    constructor(songs: Song?, id: String?, type: String?, context: Context?) {
        this.songs = songs
        this.id = id
        this.type = type
        this.context = context
    }

    override fun run() {
        val request = if (StaticData.Root.equals("网易云音乐")) {
            Request.Builder()
                .url("http://www.puremusic.com.cn:3000/song/url?id=$id")
                .addHeader("cookie", StaticData.cookie)
                .build()
        } else {
            Request.Builder()
                .url("http://www.puremusic.com.cn:3300/song/url?id=$id&type=$type")
                .build()
        }
        val response = OkHttpClient().newCall(request).execute()
        val jsonObject = response.body?.string()?.let { JSONObject(it) }
        if (StaticData.Root.equals("网易云音乐")) {
            songs?.setMusic_url(jsonObject?.getJSONArray("data")?.getJSONObject(0)?.getString("url"))
            songs?.setType(jsonObject?.getJSONArray("data")?.getJSONObject(0)?.getString("type"))
        } else {
            if (jsonObject?.getInt("result") != 400) {
                songs?.setMusic_url(jsonObject?.getString("data"))
                songs?.setType(type)
                if ("128" == songs?.getType()) {
                    songs?.setType("mp3")
                } else {
                    songs?.setFee(8)
                }
            } else {
                val pools = Executors.newCachedThreadPool()
                pools.submit(GetMusicData(songs, id, "128", context))
                pools.shutdown()
            }
        }
    }
}