package com.lie.puremusic.music.netease

import android.content.Context
import com.google.gson.Gson
import com.lie.puremusic.api.API_AUTU
import com.lie.puremusic.utils.MagicHttp

/**
 * 获取网易云推荐歌单
 */
object SingerRecommend {

    fun getSingerRecommend(context: Context, success: (ArrayList<SingerRecommendDataResult>) -> Unit) {

        val url = "${API_AUTU}/top/artists?limit=5"

        MagicHttp.OkHttpManager().getByCache(context, url, {
            try {
                val singerRecommendDataResultArrayList = Gson().fromJson(it, SingerRecommendData::class.java).artists
                success.invoke(singerRecommendDataResultArrayList)
            } catch (e: Exception) {
            }
        }, {
        })

    }

    data class SingerRecommendData(
        val artists: ArrayList<SingerRecommendDataResult>
    )

    data class SingerRecommendDataResult(
        val id: Long,
        val picUrl: String,
        val name: String
    )
}


