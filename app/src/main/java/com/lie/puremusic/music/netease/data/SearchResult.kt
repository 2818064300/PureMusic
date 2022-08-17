package com.lie.puremusic.music.netease.data

import androidx.annotation.Keep
import com.lie.puremusic.standard.data.SOURCE_NETEASE
import com.lie.puremusic.standard.data.StandardSongData

/**
 * 用户详细信息
 */
@Keep
data class SearchResult(
    val result : Result,
    val songCount : Int
) {
    @Keep
    data class Result(
        val songs : ArrayList<Song>,
    )
    data class Song(
        var name: String?,
        var id: Long?,
        var ar: ArrayList<Artist>,
        var al: Album?,
        var pop: Int?,
        var privilege : Privilege
    ) {
        data class Artist(
            var id: Long?,
            var name: String?
        ) {
            fun toCompat(): StandardSongData.StandardArtistData {
                return StandardSongData.StandardArtistData(
                    this.id,
                    this.name
                )
            }
        }

        /** 专辑 */
        data class Album(
            var id: Long?,
            var name: String?,
            var picUrl: String?
        )
        data class Privilege(
            var fee: Int?,
            var id: Long?,
            var pl: Int?,
            var maxbr: Int?,
            var flag: Int?
        )
    }

    fun getSongArrayList(): ArrayList<StandardSongData> {
        val standardPlaylistData = ArrayList<StandardSongData>()
        // 防止遍历空集合
        if (this.result.songs?.isNotEmpty() == true) {
            for (song in this.result.songs) {
                val standardArtistDataList = ArrayList<StandardSongData.StandardArtistData>()
                // song.artists
                for (i in 0..song.ar.lastIndex) {
                    val standardArtistData = StandardSongData.StandardArtistData(
                        song.ar[i].id,
                        song.ar[i].name
                    )
                    standardArtistDataList.add(standardArtistData)
                }
                val standardSongData = StandardSongData(
                    SOURCE_NETEASE,
                    song.id.toString(),
                    song.name,
                    song.al?.picUrl,
                    standardArtistDataList,
                    StandardSongData.NeteaseInfo(
                        song.privilege.fee ?: 0,
                        song.pop,
                        song.privilege.pl,
                        song.privilege.flag,
                        song.privilege.maxbr
                    ),
                    null,
                    null
                )
                standardPlaylistData.add(standardSongData)
            }
        }
        return standardPlaylistData
    }
}
