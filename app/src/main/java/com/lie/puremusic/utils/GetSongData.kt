package com.lie.puremusic.utils

import com.lie.puremusic.StaticData
import com.lie.puremusic.pojo.Singer
import com.lie.puremusic.pojo.Song
import com.lie.puremusic.pojo.SongList
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.Executors

class GetSongData : Runnable {
    private var j = 0
    private var Style = 0
    private var id: String? = null
    private var Singer: Singer? = null
    private var SongList: SongList? = null
    private var Songs: Song? = null

    constructor(j: Int, Style: Int, id: String?) {
        this.j = j
        this.Style = Style
        this.id = id
    }

    constructor(Singer: Singer?, j: Int, Style: Int, id: String?) {
        this.Singer = Singer
        this.j = j
        this.Style = Style
        this.id = id
    }

    constructor(SongList: SongList?, j: Int, Style: Int, id: String?) {
        this.SongList = SongList
        this.j = j
        this.Style = Style
        this.id = id
    }

    override fun run() {
        when (Style) {
            1 -> SongList?.getSongs()?.set(j, Song(id))
            2 -> Singer?.getSongs()?.set(j, Song(id))
            4 -> StaticData.Result.getSongs()?.set(j, Song(id))
        }
        val url = if (StaticData.Root.equals("网易云音乐")) {
            "http://www.puremusic.com.cn:3000/song/detail?ids=$id"
        } else {
            "http://www.puremusic.com.cn:3300/song?songmid=$id"
        }
        val request: Request = Request.Builder()
            .url(url)
            .build()

        val response = OkHttpClient().newCall(request).execute()
        val jsonObject = response.body?.string()?.let { JSONObject(it) }
        when (Style) {
            1 -> Songs = SongList?.getSongs()?.get(j)
            2 -> Songs = Singer?.getSongs()?.get(j)
            4 -> {
                Songs = StaticData.Result.getSongs()?.get(j)
                StaticData.Containr.add(StaticData.KeyWords)
            }
        }

        if (StaticData.Root.equals("网易云音乐")) {
            val songs = jsonObject?.getJSONArray("songs")
            if (songs != null) {
                Songs?.setName(songs.getJSONObject(0).getString("name"))
                for (i in 0 until songs.getJSONObject(0).getJSONArray("ar").length()) {
                    val singer = Singer(
                        songs.getJSONObject(0).getJSONArray("ar").getJSONObject(i).getString("id")
                    )
                    singer.setName(
                        songs.getJSONObject(0).getJSONArray("ar").getJSONObject(i).getString("name")
                    )
                    Songs?.getSingers()?.add(singer)
                }
                Songs?.setCover_url(songs.getJSONObject(0).getJSONObject("al").getString("picUrl"))
                Songs?.setFee(songs.getJSONObject(0).getInt("fee"))
                Songs?.setPop(songs.getJSONObject(0).getInt("pop"))
                Songs?.setTime(songs.getJSONObject(0).getInt("dt"))
                Songs?.setStyle("NeteaseCloudMusic")
            }
        } else {
            val songs = jsonObject?.getJSONObject("data")
            if (songs != null) {
                Songs?.setName(songs.getJSONObject("track_info").getString("name"))
                val singer = Singer(
                    songs.getJSONObject("track_info").getJSONArray("singer").getJSONObject(0)
                        .getString("mid")
                )
                singer.setName(
                    songs.getJSONObject("track_info").getJSONArray("singer").getJSONObject(0)
                        .getString("name")
                )
                Songs?.getSingers()?.add(singer)
                val pools = Executors.newCachedThreadPool()
                pools.submit(GetQQpicurl(Songs, id))
                pools.shutdown()
                Songs?.setFee(9)
                Songs?.setPop(100)
                Songs?.setTime(0)
                Songs?.setStyle("QQMusic")
            }
        }
    }
}