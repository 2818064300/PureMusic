package com.lie.puremusic.music.netease.data

import androidx.annotation.Keep

/**
 * 用户详细信息
 */
@Keep
data class UserPlaylistData(
    val playlist: ArrayList<Playlist>
) {
    @Keep
    data class Playlist(
        val id :Long,
        val name:String,
        val trackCount: Int,
        val coverImgUrl : String,
        val creator: Creator
    )
    data class Creator(
        val userId:Long,
        val nickname:String,
        val avatarUrl:String
    )

}