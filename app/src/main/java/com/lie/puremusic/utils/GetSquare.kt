package com.lie.puremusic.utils

import com.lie.puremusic.StaticData
import com.lie.puremusic.pojo.SongList
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GetSquare : Runnable{
    private var i = 0
    private var key: String? = null
    private val es = Executors.newCachedThreadPool()

    constructor(key: String?, i: Int) {
        this.key = key
        this.i = i
    }

    override fun run() {
        val request = Request.Builder()
            .url("http://47.97.35.10:3000/top/playlist?cat=$key&limit=30")
            .addHeader("cookie", StaticData.cookie)
            .build()
        val response = OkHttpClient().newCall(request).execute()
        val playlists = response.body?.string()?.let { JSONObject(it).getJSONArray("playlists") }

        for (j in 0 until playlists?.length()!!) {
            StaticData.Square.get(i)?.getSongsLists()?.add(SongList(playlists.getJSONObject(j).getString("id")))
            StaticData.Square.get(i)?.getSongsLists()?.get(j)?.setName(playlists.getJSONObject(j).getString("name"))
            StaticData.Square.get(i)?.getSongsLists()?.get(j)?.setCover_url(playlists.getJSONObject(j).getString("coverImgUrl"))
            es.submit(
                GetSonglistData2(
                    StaticData.Square.get(i)?.getSongsLists()?.get(j),
                    playlists.getJSONObject(j),
                    true
                )
            )
        }
        StaticData.Containr.add("歌单广场")
        es.shutdown()

        for (j in 0 until playlists.length()) {
            StaticData.Square.get(i)?.getSongsLists()?.add(SongList(playlists.getJSONObject(j).getString("id")))
            StaticData.Square.get(i)?.getSongsLists()?.get(j)?.setName(playlists.getJSONObject(j).getString("name"))
            StaticData.Square.get(i)?.getSongsLists()?.get(j)?.setCover_url(playlists.getJSONObject(j).getString("coverImgUrl"))
            es.submit(
                GetSonglistData2(
                    StaticData.Square.get(i)?.getSongsLists()?.get(j),
                    playlists.getJSONObject(j),
                    true
                )
            )
        }
        StaticData.Containr.add("歌单广场")
        es.shutdown()
        es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
    }
}