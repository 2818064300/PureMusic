package com.lie.puremusic.utils

import com.lie.puremusic.StaticData
import com.lie.puremusic.pojo.Singer
import com.lie.puremusic.pojo.Song
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.Executors

class GetSingerData : Runnable {
    private var i = 0
    private var id: String? = null
    private var Singer: Singer? = null

    constructor(i: Int) {
        this.i = i
    }

    constructor(id: String?) {
        this.id = id
    }

    override fun run() {
        when (StaticData.Style) {
            "SingerList" -> Singer = StaticData.Home.getSingers()?.get(i)
            "SearchSinger" -> Singer = StaticData.Result.getSingers()?.get(i)
            "Singer_ID" -> Singer = StaticData.Singer
        }

        val url = if (id == null) {
            "http://www.puremusic.com.cn:3000/artist/songs?id=" + Singer?.getId() + "&limit=50"
        } else {
            "http://www.puremusic.com.cn:3000/artist/songs?id=$id&limit=50"
        }
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val response = OkHttpClient().newCall(request).execute()
        val jsonObject = response.body?.string()?.let { JSONObject(it) }
        val songs = jsonObject?.getJSONArray("songs")
        val pools = Executors.newCachedThreadPool()
        if (songs != null) {
            for (j in 0 until songs.length()) {
                if (Singer?.getSongs()?.size!! < songs.length()) {
                    Singer?.getSongs()?.add(Song("实体$j"))
                }
                pools.submit(
                    GetSongData(
                        Singer, j, 2, songs.getJSONObject(j).getString("id")
                    )
                )
            }
        }
        pools.shutdown()
    }
}