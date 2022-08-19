package com.lie.puremusic.music.netease.data

import com.lie.puremusic.standard.data.SOURCE_NETEASE
import com.lie.puremusic.standard.data.SOURCE_NETEASE_CLOUD
import com.lie.puremusic.standard.data.StandardSongData

/**
 * 用户云盘数据
 */
data class UserCloudData(
    val data: ArrayList<SongData>,
    val count: Int, // 多少首
    val size: String, // 已用空间
    val maxSize: String, // 所有空间
    val upgradeSign: Int,
    val hasMore: Boolean, // 是否有更多
    val code: Int
) {
    data class SongData(
        val fileSize: Long,
        val album: String,
        val artist: String,
        val bitrate: Int,
        val songId: Long,
        val addTime: Long,
        val songName: String,
        val cover: Long,
        val coverId: String,
        val lyricId: String,
        val version: Int,
        val fileName: String,
        val simpleSong: SimpleSong
//        val name: String, // 歌曲名
//        val id: Long, // 歌曲 id
//        val ar: ArrayList<StandardSongData.StandardArtistData>,
//        val al: NeteaseAlbumData,
    ) {
        data class SimpleSong(
            val al: Album,
            val pop: Int,
            val privilege: PrivilegeData
        ) {
            data class Album(
                var picUrl: String
            )
            data class PrivilegeData(
                val fee: Int,
                val pl: Int?,
                val flag: Int?,
                val maxbr: Int?,
            )
        }
    }
}

fun ArrayList<UserCloudData.SongData>.toStandard(): ArrayList<StandardSongData> {
    val list = ArrayList<StandardSongData>()
    this.forEach {
        list.add(it.toStandard())
    }
    return list
}

/**
 * 转标准
 */
fun UserCloudData.SongData.toStandard(): StandardSongData {
    var picUrl = if (simpleSong.al != null) {
        simpleSong.al.picUrl
    } else {
        "https://p2.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg"
    }
    return StandardSongData(
        SOURCE_NETEASE,
        songId.toString(),
        name = songName,
        picUrl,
        arrayListOf(
            StandardSongData.StandardArtistData(
                null, this.artist
            )
        ),
        StandardSongData.NeteaseInfo(
            simpleSong.privilege.fee,
            simpleSong.pop,
            simpleSong.privilege.pl,
            simpleSong.privilege.flag,
            simpleSong.privilege.maxbr
        ),
        null,
        null
    )
}
