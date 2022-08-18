package com.lie.puremusic.standard

import com.google.gson.Gson
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.standard.data.SOURCE_NETEASE
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.utils.MagicHttp

/**
 * 搜索歌词
 * @author Moriafly
 */
object SearchLyric {

    fun getLyricString(songData: StandardSongData, success: (NeteaseSongLyric) -> Unit) {
        var url = ""
        when (songData.source) {
            SOURCE_NETEASE -> {
                url = "$API_DEFAULT/lyric?id=${songData.id}"
            }
        }

        MagicHttp.OkHttpManager().newGet(url, { response ->

            when (songData.source) {
                SOURCE_NETEASE -> {
                    if (Gson().fromJson(response, NeteaseSongLyric::class.java).lrc != null) {
                        var lyric = Gson().fromJson(response, NeteaseSongLyric::class.java)
                        success.invoke(lyric)
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
        val lrc: NeteaseSongLrc?,
        val klyric : NeteaseSongLrc?,
        val tlyric : NeteaseSongLrc?,
        val romalrc : NeteaseSongLrc?,
    ) {
        data class NeteaseSongLrc(
            val lyric: String?
        )
    }

}
