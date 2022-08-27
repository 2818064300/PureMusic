package com.lie.puremusic.service

import com.google.gson.Gson
import com.lie.puremusic.StaticData
import com.lie.puremusic.api.CloudMusicApi
import com.lie.puremusic.data.LyricViewData
import com.lie.puremusic.music.netease.SongUrl
import com.lie.puremusic.music.netease.data.SongUrlData
import com.lie.puremusic.standard.SearchLyric
import com.lie.puremusic.standard.data.SOURCE_NETEASE
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.utils.MagicHttp.runOnMainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 获取歌曲 URL
 */
object ServiceSongUrl {

    inline fun getUrl(
        song: StandardSongData?,
        crossinline success: (SongUrlData.UrlData?) -> Unit
    ) {
        when (song?.source) {
            SOURCE_NETEASE -> {
                GlobalScope.launch {
                    var url = ""
                    if (url.isEmpty())
                        if (!StaticData.isCloud) {
                            SongUrl.getSongUrlCookie(song?.id ?: "") {
                                success.invoke(it)
                            }
                        } else {
                            success.invoke(null)
                        }
                }
            }
            else -> success.invoke(null)
        }
        success.invoke(null)
    }

    fun getLyric(song: StandardSongData, success: (LyricViewData) -> Unit) {
        if (song.source == SOURCE_NETEASE) {
            SearchLyric.getLyricString(song) { it ->
                runOnMainThread {
                    if (it.lrc?.lyric != null) {
                        var secondLyric = ""
                        if (!it.klyric?.lyric.equals("")) {
                            secondLyric = it.klyric?.lyric.toString()
                        }
                        if (!it.tlyric?.lyric.equals("")) {
                            secondLyric = it.tlyric?.lyric.toString()
                        }
                        val l = LyricViewData(it.lrc.lyric, secondLyric)
                        success.invoke(l)
                    }
                }
            }
        }
    }
}