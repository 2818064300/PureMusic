package com.lie.puremusic.music.netease.data


data class NewSongData(
    val code: Int,
    val data: ArrayList<Data>
) {
    data class Data(
        val id: Long,
        val name: String,
        val popularity:Int,
        val album: PlaylistDetail.Song.Album,
        val artists: ArrayList<PlaylistDetail.Song.Artist>,
        val privilege: Privilege
    ){
        data class Privilege(
            var fee: Int?,
            var id: Long?,
            var pl: Int?,
            var maxbr: Int?,
            var flag: Int?
        )
    }
}
