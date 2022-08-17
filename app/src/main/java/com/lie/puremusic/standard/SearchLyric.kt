package com.lie.puremusic.standard

import com.google.gson.Gson
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.local.LyricParse
import com.lie.puremusic.standard.data.SOURCE_NETEASE
import com.lie.puremusic.standard.data.SOURCE_QQ
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.utils.MagicHttp

/**
 * 搜索歌词
 * @author Moriafly
 */
object SearchLyric {

    fun getLyricString(songData: StandardSongData, success: (String) -> Unit) {
        var url = ""
        when (songData.source) {
            SOURCE_NETEASE -> {
                url = "$API_DEFAULT/lyric?id=${songData.id}"
            }
            else -> {
                LyricParse.getLyric(songData.name?:"") {
                    success.invoke(it)
                }
            }
        }

        MagicHttp.OkHttpManager().newGet(url, { response ->
            var lyric = response

            when (songData.source) {
                SOURCE_NETEASE -> {
                    if (Gson().fromJson(lyric, NeteaseSongLyric::class.java).lrc != null) {
                        lyric = Gson().fromJson(lyric, NeteaseSongLyric::class.java).lrc?.lyric.toString()
                        success.invoke(lyric)
                    } else {
                        success.invoke("")
                    }
                }
            }

        }, {

        })
    }


    /**
     * 网易云歌词数据类
     */
    data class NeteaseSongLyric(
        val lrc: NeteaseSongLrc?
    ) {
        data class NeteaseSongLrc(
            val lyric: String?
        )
    }

}
