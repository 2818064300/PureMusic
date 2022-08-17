package com.lie.puremusic.music.netease.data

import com.lie.puremusic.standard.data.SOURCE_NETEASE
import com.lie.puremusic.standard.data.StandardSongData

data class PlaylistDetail(
    var songs: ArrayList<Song>?,
    var privileges: ArrayList<Privilege>?
) {
    data class Song(
        var name: String?,
        var id: Long?,
        var ar: ArrayList<Artist>,
        var al: Album?,
        var pop: Int?
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
    }

    data class Privilege(
        var fee: Int?,
        var id: Long?,
        var pl: Int?,
        var maxbr: Int?,
        var flag: Int?
    )

    fun getSongArrayList(): ArrayList<StandardSongData> {
        val standardPlaylistData = ArrayList<StandardSongData>()
        // 防止遍历空集合
        if (this.songs?.isNotEmpty() == true) {
            for ((index, song) in this.songs!!.withIndex()) {
                val standardArtistDataList = ArrayList<StandardSongData.StandardArtistData>()
                // song.artists
                for (i in 0..song.ar.lastIndex) {
                    val standardArtistData = StandardSongData.StandardArtistData(
                        song.ar[i].id,
                        song.ar[i].name
                    )
                    standardArtistDataList.add(standardArtistData)
                }

                val privileges = this.privileges?.get(index)

                val standardSongData = StandardSongData(
                    SOURCE_NETEASE,
                    song.id.toString(),
                    song.name,
                    song.al?.picUrl,
                    standardArtistDataList,
                    StandardSongData.NeteaseInfo(
                        privileges?.fee ?: 0,
                        song.pop,
                        privileges?.pl,
                        privileges?.flag,
                        privileges?.maxbr
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