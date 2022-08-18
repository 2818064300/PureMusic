package com.lie.puremusic.music.netease.data

data class TopListData(
    val code: Int,
    val list: ArrayList<ListData>
) {
    data class ListData(
        val tracks: ArrayList<TracksData>?,
        val updateFrequency: String,
        val description: String,
        val coverImgUrl: String,
        val name: String,
        val id: Long,
        val playCount:Long
    ) {
        data class TracksData(
            val first: String,
            val second: String,
        )
    }
}
