package com.lie.puremusic.utils

import com.lie.puremusic.StaticData
import com.lie.puremusic.pojo.Song
import com.lie.puremusic.pojo.SongList
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.Executors

class GetSonglistData : Runnable {
    private var i = 0
    private var id: String? = null
    private var SongList: SongList? = null
    private val limit = 100
    private var offset = 0

    constructor(i: Int, offset: Int) {
        this.i = i
        this.offset = offset
    }

    constructor(id: String?, offset: Int) {
        this.id = id
        this.offset = offset
    }

    override fun run() {
        when (StaticData.Style) {
            "PopularList" -> SongList = StaticData.Home.getSongsLists()?.get(i)
            "Leaderboard" -> SongList = StaticData.Home.getSongsLists()?.get(i)
            "SearchList" -> SongList = StaticData.Result.getSongLists()?.get(i)
            "List_ID" -> SongList = StaticData.SongList
            "UserList" -> SongList = StaticData.User?.getSongLists()?.get(i)
            "SquareList" -> StaticData.Square.get(StaticData.Square_SelectID)?.getSongsLists()
                ?.get(i)
        }
        val url = if (id == null) {
            "http://www.puremusic.com.cn:3000/playlist/track/all?id=" + SongList?.getId() + "&limit=" + limit + "&offset=" + offset
        } else {
            "http://www.puremusic.com.cn:3000/playlist/track/all?id=$id&limit=$limit&offset=$offset"
        }
        val request = if (StaticData.Style.equals("List_ID")) {
            Request.Builder()
                .url(
                    "http://www.puremusic.com.cn:3000/playlist/track/all?id=" + StaticData.User?.getFavorite()
                        ?.getId()
                )
                .addHeader("cookie", StaticData.cookie)
                .build()
        } else {
            Request.Builder()
                .url(url)
                .build()
        }
        val response = OkHttpClient().newCall(request).execute()
        val jsonObject = response.body?.string()?.let { JSONObject(it) }
        val songs = jsonObject?.getJSONArray("songs")
        if (songs != null) {
            SongList?.setCount(songs.length())
            val pools = Executors.newCachedThreadPool()
            for (j in 0 until songs.length()) {
                if (SongList?.getSongs()?.size!! < songs.length() + offset * 30) {
                    SongList?.getSongs()?.add(Song("null" + j + offset * 30))
                }
                pools.submit(
                    GetSongData(
                        SongList, j + offset * 30, 1, songs.getJSONObject(j).getString("id")
                    )
                )
            }
            pools.shutdown()
        }
    }
}