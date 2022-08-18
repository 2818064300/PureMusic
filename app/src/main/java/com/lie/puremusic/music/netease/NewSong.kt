package com.lie.puremusic.music.netease

import android.content.Context
import com.google.gson.Gson
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.NewSongData
import com.lie.puremusic.standard.data.SOURCE_NETEASE
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.utils.MagicHttp
import java.lang.Exception

/**
 * 新歌速递
 */
object NewSong {

    private const val url = "${API_DEFAULT}/top/song?type=0&limit=12" // 获取歌单链接

    fun getNewSong(context: Context, success: (ArrayList<StandardSongData>) -> Unit) {
        MagicHttp.OkHttpManager().getByCache(context, url, { string ->
            try {
                val newSongData = Gson().fromJson(string, NewSongData::class.java)

                val standardSongDataList = ArrayList<StandardSongData>()

                newSongData.data.forEach{
                    val standardArtistData = ArrayList<StandardSongData.StandardArtistData>()
                    it.artists.forEach { artist ->
                        standardArtistData.add(StandardSongData.StandardArtistData(artist.id, artist.name))
                    }
                    val standardSongData = StandardSongData(
                        SOURCE_NETEASE,
                        it.id.toString(),
                        it.name,
                        it.album.picUrl,
                        standardArtistData,
                        StandardSongData.NeteaseInfo(
                            it.privilege.fee, it.privilege.pl,it.popularity, it.privilege.flag, it.privilege.maxbr
                        ), null, null
                    )
                    standardSongDataList.add(standardSongData)
                }
                success.invoke(standardSongDataList)
            } catch (e: Exception) {

            }
        }, {

        })


    }

}