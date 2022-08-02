package com.lie.puremusic.utils

import com.lie.puremusic.StaticData
import com.lie.puremusic.pojo.Singer
import com.lie.puremusic.pojo.Song
import com.lie.puremusic.pojo.SongList
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.Executors

class GetResultData : Runnable {
    private var keywords: String? = null

    constructor(keywords: String?) {
        this.keywords = keywords
    }

    override fun run() {
        var url = "暂无"
        if (StaticData.Root.equals("网易云音乐")) {
            when (StaticData.SelectID) {
                0 -> url =
                    "http://www.puremusic.com.cn:3000/cloudsearch?keywords=%20$keywords&type=1"
                2 -> url =
                    "http://www.puremusic.com.cn:3000/cloudsearch?keywords=%20$keywords&type=100"
                3 -> url =
                    "http://www.puremusic.com.cn:3000/cloudsearch?keywords=%20$keywords&type=1000"
                4 -> url =
                    "http://www.puremusic.com.cn:3000/cloudsearch?keywords=%20$keywords&type=1006"
            }
        } else {
            when (StaticData.SelectID) {
                0 -> url = "http://www.puremusic.com.cn:3300/search?key=$keywords"
                4 -> "http://www.puremusic.com.cn:3300/search?key=$keywords?t=7"
            }
        }
        val pools = Executors.newCachedThreadPool()
        val request = Request.Builder()
            .url(url)
            .build()
        val response = OkHttpClient().newCall(request).execute()
        val jsonObject = response.body?.string()?.let { JSONObject(it) }
        if (StaticData.Root.equals("网易云音乐")) {
            if (StaticData.SelectID == 0 || StaticData.SelectID == 4) {
                val result = jsonObject?.getJSONObject("result")?.getJSONArray("songs")
                if (result != null) {
                    for (j in 0 until result.length()) {
                        if (StaticData.Result.getSongs()?.size!! < result.length()) {
                            StaticData.Result.getSongs()?.add(Song("实体$j"))
                        }
                        pools.execute(
                            GetSongData(
                                j, 4, result.getJSONObject(j).getString("id")
                            )
                        )
                    }
                }
                pools.shutdown()
            }
            if (StaticData.SelectID == 2) {
                val result = jsonObject?.getJSONObject("result")?.getJSONArray("artists")
                if (result != null) {
                    for (j in 0 until result.length()) {
                        if (StaticData.Result.getSingers()?.size!! < result.length()) {
                            StaticData.Result.getSingers()?.add(Singer("实体$j"))
                        }
                        pools.submit(
                            GetSingerData2(
                                j,
                                result.getJSONObject(j).getString("id"),
                                result.getJSONObject(j).getString("name"),
                                result.getJSONObject(j).getString("picUrl")
                            )
                        )
                    }
                }
                pools.shutdown()
            }
            if (StaticData.SelectID == 3) {
                val result = jsonObject?.getJSONObject("result")?.getJSONArray("playlists")
                if (result != null) {
                    for (j in 0 until result.length()) {
                        if (StaticData.Result.getSongLists()?.size!! < result.length()) {
                            StaticData.Result.getSongLists()?.add(SongList("实体$j"))
                        }
                        pools.submit(
                            GetSonglistData2(
                                StaticData.Result.getSongLists()?.get(j), result.getJSONObject(j)
                            )
                        )
                    }
                }
                pools.shutdown()
            }
        } else {
            val result = jsonObject?.getJSONObject("data")?.getJSONArray("list")
            if (result != null) {
                for (j in 0 until result.length()) {
                    if (StaticData.Result.getSongs()?.size!! < result.length()) {
                        StaticData.Result.getSongs()?.add(Song("实体$j"))
                    }
                    pools.submit(
                        GetSongData(
                            j, 4, result.getJSONObject(j).getString("songmid")
                        )
                    )
                }
            }
            pools.shutdown()
        }
    }
}