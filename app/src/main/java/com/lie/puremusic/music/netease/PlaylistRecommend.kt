package com.lie.puremusic.music.netease

import android.content.Context
import com.google.gson.Gson
import com.lie.puremusic.api.API_AUTU
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.utils.MagicHttp

/**
 * 获取网易云推荐歌单
 */
object PlaylistRecommend {

    fun getPlaylistRecommend(context: Context, success: (ArrayList<PlaylistRecommendDataResult>) -> Unit) {

        val url = "${API_DEFAULT}/personalized?limit=60"

        MagicHttp.OkHttpManager().getByCache(context, url, {
            try {
                val playlistRecommendDataResultArrayList = Gson().fromJson(it, PlaylistRecommendData::class.java).result
                success.invoke(playlistRecommendDataResultArrayList)
            } catch (e: Exception) {
            }
        }, {
        })

    }

    data class PlaylistRecommendData(
        val result: ArrayList<PlaylistRecommendDataResult>
    )

    data class PlaylistRecommendDataResult(
        val id: Long,
        val picUrl: String,
        val name: String,
        val playCount: Long, // 播放数
    )

}


